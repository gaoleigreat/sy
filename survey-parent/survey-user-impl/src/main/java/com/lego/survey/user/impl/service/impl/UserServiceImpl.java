package com.lego.survey.user.impl.service.impl;

import com.lego.survey.project.feign.GroupClient;
import com.lego.survey.project.feign.ProjectClient;
import com.lego.survey.project.feign.SectionClient;
import com.lego.survey.project.feign.WorkspaceClient;
import com.lego.survey.project.model.entity.*;
import com.lego.survey.project.model.vo.GroupVo;
import com.lego.survey.project.model.vo.ProjectVo;
import com.lego.survey.project.model.vo.SectionVo;
import com.lego.survey.user.impl.repository.UserRepository;
import com.lego.survey.user.impl.service.IConfigService;
import com.lego.survey.user.impl.service.IUserService;
import com.lego.survey.user.model.entity.*;
import com.lego.survey.user.model.vo.ConfigOptionVo;
import com.lego.survey.user.model.vo.UserAddVo;
import com.lego.survey.user.model.vo.UserVo;
import com.survey.lib.common.consts.RespConsts;
import com.survey.lib.common.page.PagedResult;
import com.survey.lib.common.vo.RespDataVO;
import com.survey.lib.common.vo.RespVO;
import com.survey.lib.common.vo.RespVOBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author yanglf
 * @description
 * @since 2018/12/28
 **/
@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SectionClient sectionClient;

    @Autowired
    private GroupClient groupClient;

    @Autowired
    private ProjectClient projectClient;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private IConfigService iConfigService;

    @Autowired
    private WorkspaceClient workspaceClient;

    @Override
    public User queryByUserNameOrPhone(String loginName) {
        User user = userRepository.findUserByPhoneAndValid(loginName, 0);
        if (user == null) {
            user = userRepository.findUserByUserNameAndValid(loginName, 0);
        }
        return user;
    }

    @Override
    public User queryByPhone(String phone) {
        return userRepository.findUserByPhoneAndValid(phone, 0);
    }

    @Override
    public User queryByUserName(String userName) {
        return userRepository.findUserByUserNameAndValid(userName, 0);
    }

    @Override
    public RespVO modify(UserAddVo userAddVo) {
        //  修改用户信息
        String userId = userAddVo.getId();
        User user = userRepository.findUserByIdAndValid(userId, 0);
        if (user == null) {
            return RespVOBuilder.failure();
        }
        User loadUser = getUser(userAddVo, user);
        userRepository.save(loadUser);
        return RespVOBuilder.success();
    }

    @Override
    public RespVO delete(String userId) {
        // 删除用户信息
        User user = userRepository.findUserByIdAndValid(userId, 0);
        user.setValid(1);
        userRepository.save(user);
        // TODO 更新关联表状态

        return RespVOBuilder.success();
    }

    @Override
    public RespVO modifyPhone(String userId, String phone, String activeCode, String newPhone, String newActiveCode) {
        // 验证手机验证码
        // 修改手机号
        User user = userRepository.findUserByIdAndValid(userId, 0);
        user.setPhone(newPhone);
        user.setUpdateTime(new Date());
        userRepository.save(user);
        return RespVOBuilder.success();
    }

    @Override
    public RespVO modifyPassword(String userId, String phone, String activeCode, String password) {
        // 验证验证码
        // 修改密码
        User user = userRepository.findUserByIdAndValid(userId, 0);
        user.setPassWord(password);
        user.setUpdateTime(new Date());
        userRepository.save(user);
        return RespVOBuilder.success();
    }

    @Override
    public RespVO create(UserAddVo userAddVo) {
        // 创建用户
        User user = getUser(userAddVo, null);
        userRepository.save(user);
        // 注册用户
        return RespVOBuilder.success();
    }

    private User getUser(UserAddVo userAddVo, User queryUser) {
        User user = userAddVo.loadUser(queryUser);
        String group = userAddVo.getGroup();
        if (group != null) {
            RespVO<GroupVo> respVO = groupClient.query(group);
            if (respVO.getRetCode() == RespConsts.SUCCESS_RESULT_CODE) {
                GroupVo info = respVO.getInfo();
                if (info != null) {
                    user.setGroup(OwnGroup.builder().id(info.getId())
                            .name(info.getName()).build());
                }
            }
        }
        List<String> projects = userAddVo.getProject();
        if (projects != null) {
            List<OwnProject> ownProjects = new ArrayList<>();
            for (String project : projects) {
                RespVO<ProjectVo> respVO = projectClient.query(project);
                if (respVO.getRetCode() == RespConsts.SUCCESS_RESULT_CODE) {
                    ProjectVo info = respVO.getInfo();
                    if (info != null) {
                        ownProjects.add(OwnProject.builder()
                                .id(info.getId())
                                .name(info.getName())
                                .code(info.getCode()).build());
                    }
                }
                user.setOwnProjects(ownProjects);
            }
        }
        List<String> sections = userAddVo.getSection();
        if (sections != null) {
            List<OwnSection> ownSections = new ArrayList<>();
            for (String section : sections) {
                RespVO<SectionVo> respVO = sectionClient.query(section);
                if (respVO.getRetCode() == RespConsts.SUCCESS_RESULT_CODE) {
                    SectionVo info = respVO.getInfo();
                    if (info != null) {
                        ownSections.add(OwnSection.builder().
                                id(info.getId())
                                .name(info.getName())
                                .code(info.getCode())
                                .build());
                    }
                }
                user.setOwnSections(ownSections);
            }
        }
        return user;
    }

    @Override
    public RespVO<UserVo> queryUserById(String id) {
        User user = userRepository.findUserByIdAndValid(id, 0);
        OwnGroup ownGroup = user.getGroup();
        UserVo userVo = UserVo.builder()
                .userName(user.getUserName())
                .cardId(user.getCardId())
                .groupName(ownGroup != null ? ownGroup.getName() : "")
                .id(user.getId())
                .name(user.getName())
                .role(user.getRole())
                .projectName("")
                .sectionName("")
                .phone(user.getPhone())
                .build();
        RespVO<RespDataVO<Section>> dataVORespVO = sectionClient.queryByMasterId(id);
        if (dataVORespVO.getRetCode() == RespConsts.SUCCESS_RESULT_CODE) {
            List<Section> list = dataVORespVO.getInfo().getList();
            if (list != null && list.size() > 0) {
                Section section = list.get(0);
                userVo.setSectionName(section.getName());
                OwnerProject ownerProject = section.getOwnerProject();
                if (ownerProject != null) {
                    userVo.setProjectName(ownerProject.getName());
                }
            }
        }
        List<String> userService = getUserService(id);
        userVo.setServices(userService);
        return RespVOBuilder.success(userVo);
    }

    @Override
    public PagedResult<UserAddVo> queryList(int pageIndex,
                                            int pageSize,
                                            String projectCode,
                                            String sectionCode,
                                            String role,
                                            String groupId) {
        List<UserAddVo> userAddVos = new ArrayList<>();
        Query query = new Query();
        query.with(PageRequest.of(pageIndex - 1, pageSize));
        Criteria criteria = new Criteria();
        criteria.and("valid").is(0);
        if (!StringUtils.isEmpty(groupId)) {
            criteria.and("group._id").is(groupId);
        }
        if (!StringUtils.isEmpty(role)) {
            criteria.and("role").is(role);
        }
        if (!StringUtils.isEmpty(projectCode)) {
            criteria.and("ownProjects.code").is(projectCode);
        }
        if (!StringUtils.isEmpty(sectionCode)) {
            criteria.and("ownSections.code").is(sectionCode);
        }
        query.addCriteria(criteria);
        List<User> userList = mongoTemplate.find(query, User.class, "user");
        for (User user : userList) {
            UserAddVo userAddVo = getUserAddVo(user);
            userAddVos.add(userAddVo);
        }
        return getJsonObjectPagedResult(pageIndex, pageSize, query, userAddVos, mongoTemplate, "user");
    }

    private UserAddVo getUserAddVo(User user) {
        UserAddVo userAddVo = UserAddVo.builder().role(user.getRole())
                .userName(user.getUserName())
                .phone(user.getPhone())
                .name(user.getName())
                .id(user.getId())
                .cardId(user.getCardId())
                .valid(user.getValid())
                .build();
        if (user.getGroup() != null) {
            userAddVo.setGroup(user.getGroup().getName());
        }
        List<String> permissions = user.getPermission();
        if (!CollectionUtils.isEmpty(permissions)) {
            List<String> permissionDesc = new ArrayList<>();
            Config config = iConfigService.queryByName("权限配置");
            if (config != null) {
                for (String permission : permissions) {
                    List<ConfigOptionVo> options = config.getOption();
                    if (!CollectionUtils.isEmpty(options)) {
                        for (ConfigOptionVo option : options) {
                            String name = option.getName();
                            if (name.equals(permission)) {
                                permissionDesc.add(option.getDesc());
                            }
                        }
                    }
                }
            }
            userAddVo.setPermission(permissionDesc);
        }


        List<OwnProject> ownProjects = user.getOwnProjects();
        if (!CollectionUtils.isEmpty(ownProjects)) {
            List<String> collect = ownProjects.stream().map(OwnProject::getName).collect(Collectors.toList());
            userAddVo.setProject(collect);
        }
        List<OwnSection> ownSections = user.getOwnSections();
        if (!CollectionUtils.isEmpty(ownSections)) {
            List<String> sections = ownSections.stream().map(OwnSection::getName).collect(Collectors.toList());
            userAddVo.setSection(sections);
        }
        String role1 = user.getRole();
        if (role1 != null) {
            Config config = iConfigService.queryByName("角色配置");
            if (config != null) {
                List<ConfigOptionVo> option = config.getOption();
                for (ConfigOptionVo opt : option) {
                    if (opt.getName().equals(role1)) {
                        userAddVo.setRole(opt.getDesc());
                        break;
                    }
                }
            }
        }
        return userAddVo;
    }

    @Override
    public UserAddVo findByUserId(String userId) {
        User user = userRepository.findUserByIdAndValid(userId, 0);
        if (user != null) {
            return user.loadUserVo();
        }
        return null;
    }


    private PagedResult<UserAddVo> getJsonObjectPagedResult(int pageIndex, int pageSize, Query
            query, List<UserAddVo> objectList, MongoTemplate mongoTemplate, String tableName) {
        PagedResult<UserAddVo> pagedResult = new PagedResult<>();
        Long count = mongoTemplate.count(query, Long.class, tableName);
        pagedResult.setResultList(objectList);
        int totalPage = count.intValue() % pageSize == 0 ? count.intValue() / pageSize : count.intValue() / pageSize + 1;
        pagedResult.setPage(new com.survey.lib.common.page.Page(pageIndex, pageSize, 0, count, totalPage));
        return pagedResult;
    }


    private List<String> getUserService(String userId) {
        List<String> services = new ArrayList<>();
        RespVO<RespDataVO<Section>> dataVORespVO = sectionClient.queryByMasterId(userId);
        if (dataVORespVO.getRetCode() == RespConsts.SUCCESS_RESULT_CODE) {
            List<Section> sectionList = dataVORespVO.getInfo().getList();
            for (Section section : sectionList) {
                List<String> service = section.getService();
                services.addAll(service);
            }
        }
        return services;
    }
}
