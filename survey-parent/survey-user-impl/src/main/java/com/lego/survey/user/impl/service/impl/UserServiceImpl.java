package com.lego.survey.user.impl.service.impl;

import com.lego.survey.project.feign.SectionClient;
import com.lego.survey.project.model.entity.OwnWorkspace;
import com.lego.survey.project.model.entity.OwnerProject;
import com.lego.survey.project.model.entity.Section;
import com.lego.survey.project.model.entity.Surveyer;
import com.lego.survey.user.impl.repository.UserRepository;
import com.lego.survey.user.impl.service.IUserService;
import com.lego.survey.user.model.entity.OwnGroup;
import com.lego.survey.user.model.entity.User;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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
    private MongoTemplate mongoTemplate;

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
    public RespVO modify(String userId, String userName, String name, String cardId) {
        //  修改用户信息
        User user = userRepository.findUserByIdAndValid(userId, 0);
        user.setName(name);
        user.setCardId(cardId);
        user.setUpdateTime(new Date());
        userRepository.save(user);
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
    public RespVO create(User user) {
        // 创建用户
        user.setValid(0);
        Date currentDate = new Date();
        user.setCreateTime(currentDate);
        user.setUpdateTime(currentDate);
        //user.setPassWord(SecurityUtils.encryptionWithMd5(user.getPassWord()));
        userRepository.save(user);
        // 注册用户
        return RespVOBuilder.success();
    }

    @Override
    public RespVO<UserVo> queryUserById(String id) {
        User user = userRepository.findUserByIdAndValid(id, 0);
        OwnGroup ownGroup = user.getGroup();
        UserVo userVo = UserVo.builder()
                .userName(user.getUserName())
                .cardId(user.getCardId())
                .groupName(ownGroup != null ? ownGroup.getGroupName() : "")
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
    public PagedResult<UserVo> queryList(int pageIndex,
                                         int pageSize,
                                         String projectId,
                                         String sectionId,
                                         String role,
                                         String groupId) {
        List<UserVo> userVos = new ArrayList<>();
        Query query = new Query();
        query.with(PageRequest.of(pageIndex - 1, pageSize));
        Criteria criteria = new Criteria();
        if (!StringUtils.isEmpty(projectId)) {
            criteria.and("group._id").is(groupId);
        }
        if (!StringUtils.isEmpty(role)) {
            criteria.and("role").is(role);
        }
        if (!StringUtils.isEmpty(projectId)) {
            criteria.and("projectId").is(projectId);
        }
        if (!StringUtils.isEmpty(sectionId)) {
            criteria.and("sectionId").is(sectionId);
        }
        query.addCriteria(criteria);
        List<User> userList = mongoTemplate.find(query, User.class, "user");
        for (User user : userList) {
            UserVo userVo = UserVo.builder().role(user.getRole())
                    .userName(user.getUserName())
                    .phone(user.getPhone())
                    .name(user.getName())
                    .cardId(user.getCardId())
                    .id(user.getId())
                    .permissions(user.getPermission())
                    .build();
            if (user.getGroup() != null) {
                userVo.setGroupName(user.getGroup().getGroupName());
            }
            RespVO<RespDataVO<Section>> dataVORespVO = sectionClient.queryByMasterId(user.getId());
            if (dataVORespVO.getRetCode() == RespConsts.SUCCESS_RESULT_CODE) {
                setProjectInfo(user, userVo, dataVORespVO);
            }
            List<String> userService = getUserService(user.getId());
            userVo.setServices(userService);
            userVos.add(userVo);
        }
        return getJsonObjectPagedResult(pageIndex,pageSize,query,userVos,mongoTemplate,"user");
    }

    @Override
    public User findByUserId(String userId) {
        Optional<User> optional = userRepository.findById(userId);
        return optional.orElse(null);
    }

    private void setProjectInfo(User user, UserVo userVo, RespVO<RespDataVO<Section>> dataVORespVO) {
        List<Section> sectionList = dataVORespVO.getInfo().getList();
        if (sectionList != null && sectionList.size() > 0) {
            Section section = sectionList.get(0);
            OwnerProject ownerProject = section.getOwnerProject();
            List<OwnWorkspace> workSpaces = section.getWorkSpace();
            if(!CollectionUtils.isEmpty(workSpaces)){
                workSpaces.forEach(workSpace ->{
                    List<Surveyer> surveyers = workSpace.getSurveyer();
                    if(!CollectionUtils.isEmpty(surveyers)){
                        for (Surveyer surveyer : surveyers) {
                            if (surveyer.getId().equals(user.getId())) {
                                userVo.setWorkSpace(workSpace.getName());
                                break;
                            }
                        }
                    }
                });
            }

            if (ownerProject != null) {
                userVo.setProjectName(ownerProject.getName());
            }
            userVo.setSectionName(section.getName());
        }
    }


    private PagedResult<UserVo> getJsonObjectPagedResult(int pageIndex, int pageSize, Query query, List<UserVo> objectList, MongoTemplate mongoTemplate, String tableName) {
        PagedResult<UserVo> pagedResult =new PagedResult<>();
        Long count = mongoTemplate.count(query, Long.class,tableName);
        pagedResult.setResultList(objectList);
        int totalPage = count.intValue() % pageSize == 0 ? count.intValue() / pageSize : count.intValue() / pageSize + 1;
        pagedResult.setPage(new com.survey.lib.common.page.Page(pageIndex,pageSize,0,count,totalPage));
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
