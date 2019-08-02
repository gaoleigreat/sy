package com.lego.survey.auth.impl.service.imp;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lego.survey.auth.impl.mapper.ResourcesMapper;
import com.lego.survey.auth.impl.mapper.RoleResourcesMapper;
import com.lego.survey.auth.impl.service.IResourcesService;
import com.lego.survey.auth.model.entity.Resources;
import com.lego.survey.auth.model.entity.RoleResources;
import com.lego.survey.user.feign.ConfigClient;
import com.survey.lib.common.consts.RespConsts;
import com.survey.lib.common.vo.RespVO;
import com.survey.lib.common.vo.RespVOBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * @author yanglf
 * @description
 * @since 2019/7/10
 **/
@Service
public class ResoucesServiceImpl implements IResourcesService {


    @Autowired
    private ResourcesMapper resourcesMapper;

    @Autowired
    private RoleResourcesMapper roleResourcesMapper;


    @Override
    public List<Resources> findList(Resources resources) {
        return resourcesMapper.findList(resources);
    }

    @Override
    public List<Map<String, Object>> findTree(Resources resources) {
        List<Resources> resourcesList = resourcesMapper.findList(resources);

        Map<String, Map<String, List<Resources>>> map = new HashMap<>(16);
        if (CollectionUtils.isEmpty(resourcesList)) {
            return Collections.EMPTY_LIST;
        }

        for (Resources r : resourcesList) {
            if (null == r || StringUtils.isEmpty(r.getScope()) || StringUtils.isEmpty(r.getPrName()) || StringUtils.isEmpty(r.getRId())) {
                continue;
            }
            if (map.containsKey(r.getScope())) {
                Map<String, List<Resources>> m1 = map.get(r.getScope());
                if (m1.containsKey(r.getRName())) {
                    m1.get(r.getRName()).add(r);
                } else {
                    List<Resources> list = new ArrayList<>();
                    list.add(r);
                    m1.put(r.getRName(), list);
                }
            } else {
                Map<String, List<Resources>> m1 = new HashMap<>(16);
                List<Resources> list = new ArrayList<>();
                list.add(r);
                m1.put(r.getRName(), list);
                map.put(r.getScope(), m1);
            }
        }
        //转为前台的json格式
        List<Map<String, Object>> resultList = new ArrayList<>();

        for (String key : map.keySet()) {
            Map<String, Object> m1 = new HashMap<>(16);

            Map<String, List<Resources>> mm = map.get(key);
            List<Map<String, Object>> list = new ArrayList<>();

            for (String k : mm.keySet()) {
                List<Resources> permissionList = mm.get(k);
                Map<String, Object> m2 = new HashMap<>(16);
                m2.put("rName", k);
                m2.put("children", permissionList);
                list.add(m2);
            }

            m1.put("rName", key);
            m1.put("children", list);
            resultList.add(m1);
        }
        return resultList;
    }

    @Override
    public RespVO insertList(List<Resources> resources) {
        if (CollectionUtils.isEmpty(resources)) {
            return RespVOBuilder.failure();
        }
        resourcesMapper.insertList(resources);

        //TODO 更新 用户权限

        return RespVOBuilder.success();
    }

    @Override
    public RespVO deleteList(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return RespVOBuilder.failure(RespConsts.DATA_ERROR, "参数缺失");
        }
        //删除权限点
        resourcesMapper.deleteList(ids);
        //删除已绑定角色
        //rolePermissionService.deleteList(ids);
        //TODO 更新 用户权限
        return RespVOBuilder.success();
    }

    @Override
    public RespVO save(String scope, List<Resources> resources) {
        if (StringUtils.isEmpty(scope)) {
            return RespVOBuilder.failure();
        }

        Resources queryParam = new Resources();
        queryParam.setScope(scope);
        List<Resources> origin = findList(queryParam);
        List<Long> originIds = new ArrayList<>();

        List<Resources> temp = new ArrayList<>();
        Set<String> set = new HashSet<>();
        for (Resources r : resources) {
            if (StringUtils.isEmpty(r.getRId()) || StringUtils.isEmpty(r.getPrId())) {
                continue;
            }
            String key = r.getRId() + "$" + r.getPrId();
            if (!set.contains(key)) {
                r.setScope(scope);
                set.add(key);
                temp.add(r);
            }
        }
        resources.clear();
        resources.addAll(temp);


        if (CollectionUtils.isEmpty(origin)) {
            insertList(resources);
            return RespVOBuilder.success();
        }

        Map<String, Resources> originMap = new HashMap<>(16);
        for (Resources r : origin) {
            originMap.put(r.getRId() + "$" + r.getPrId(), r);
            originIds.add(r.getId());
        }

        Map<String, Resources> newMap = new HashMap<>(16);

        List<Long> deletes = new ArrayList<Long>();
        List<Resources> inserts = new ArrayList<Resources>();

        if (CollectionUtils.isEmpty(resources)) {
            deleteList(originIds);
            return RespVOBuilder.success();
        } else {
            for (Resources r : resources) {
                r.setScope(scope);
                String key = r.getRId() + "$" + r.getPrId();
                newMap.put(key, r);
                if (!originMap.containsKey(key)) {
                    inserts.add(r);
                }
            }
        }

        for (Resources r : origin) {
            String key = r.getRId() + "$" + r.getPrId();
            if (!newMap.containsKey(key)) {
                deletes.add(r.getId());
            }
        }

        if (!CollectionUtils.isEmpty(deletes)) {
            deleteList(deletes);
        }

        if (!CollectionUtils.isEmpty(inserts)) {
            insertList(inserts);
        }
         //TODO 更新 用户权限
        return RespVOBuilder.success();
    }

    @Override
    public List<String> queryRoleResource(String role) {
            List<String> resourcesList=new ArrayList<>();
            List<RoleResources> roleResources = roleResourcesMapper.findByRole(role);
            if(!CollectionUtils.isEmpty(roleResources)){
                for (RoleResources rResources : roleResources) {
                    Long resourcesId = rResources.getResourcesId();
                    Resources resources = resourcesMapper.queryById(resourcesId);
                    String rId = resources.getRId();
                    String prId = resources.getPrId();
                    String scope = resources.getScope();
                    if(!StringUtils.isEmpty(rId) && !StringUtils.isEmpty(prId) && !StringUtils.isEmpty(scope)){
                        resourcesList.add(scope+"$"+rId+"$"+prId);
                    }
                }
        }
        return resourcesList;
    }
}
