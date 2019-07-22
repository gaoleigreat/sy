package com.lego.survey.permission.init;

import com.lego.survey.auth.feign.ResourcesClient;
import com.lego.survey.auth.model.entity.Resources;
import com.lego.survey.base.annotation.Operation;
import com.lego.survey.base.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.MethodMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class ResourcesInit implements ApplicationRunner {

    @Value("${spring.application.name}")
    private String serviceName;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    private static String serviceKey;

    private static Set<String> serviceAuthorized = new HashSet<>();

    @Autowired
    private ResourcesClient resourcesClient;

    private void initUserPermission() {
        //1.扫描权限点
        List<Resources> resources = getResource();
        //2.权限点插入数据库
      //  resourcesClient.saveUserPermissionList(serviceName, resources);
    }

   /* private void initServicePermission(){
        BaseService baseService = new BaseService();
        baseService.setServiceName(serviceName);
        List<BaseService> baseServices = securityFeignClient.findBaseServiceList(serviceName);
        if (CollectionUtils.isEmpty(baseServices)) {
            baseServices = new ArrayList<>();
            baseService.setContextPath(contextPath);
            baseServices.add(baseService);
            securityFeignClient.insertBaseServiceList(baseServices);
        }

        ServicePermission servicePermission = new ServicePermission();
        servicePermission.setServiceName(serviceName);
        List<ServicePermission> servicePermissions = securityFeignClient.findServicePermissionList(serviceName);
        if (!CollectionUtils.isEmpty(servicePermissions)) {
            Set<String> set = new HashSet<>();
            for (ServicePermission permission : servicePermissions) {
                if (null == permission || StringUtils.isEmpty(permission.getAuthorized())) {
                    continue;
                }
                set.add(permission.getAuthorized());
            }
            ServicePermissionContext.setServiceAuthorized(set);
        }
    }*/

    private List<Resources> getResource() {
        List<Resources> resourcesList = new ArrayList<>();
        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory();
        String basePath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + "/com/lego/survey/**/controller/*Controller.class";

        try {

            org.springframework.core.io.Resource[] resources = resourcePatternResolver.getResources(basePath);
            TypeFilter typeFilter = new AnnotationTypeFilter(Resource.class);

            for (org.springframework.core.io.Resource resource : resources) {

                MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(resource);
                if (typeFilter.match(metadataReader, metadataReaderFactory)) {
                    String rId = "";
                    String rName = "";
                    AnnotationMetadata annotationMetadata = metadataReader.getAnnotationMetadata();
                    Map<String, Object> classMap = annotationMetadata.getAnnotationAttributes(Resource.class.getName());
                    for (Map.Entry<String, Object> entry : classMap.entrySet()) {
                        if ("value".endsWith(entry.getKey())) {
                            rId = (String) entry.getValue();
                        }
                        if ("desc".endsWith(entry.getKey())) {
                            rName = (String) entry.getValue();
                        }
                    }

                    Set<MethodMetadata> set = annotationMetadata.getAnnotatedMethods(Operation.class.getName());
                    for (MethodMetadata methodMetadata : set) {
                        Resources r = new Resources();
                        r.setRId(rId);
                        r.setRName(rName);
                        Map<String, Object> methodMap = methodMetadata.getAnnotationAttributes(Operation.class.getName());
                        for (Map.Entry<String, Object> entry : methodMap.entrySet()) {
                            if ("value".endsWith(entry.getKey())) {
                                r.setPrId((String) entry.getValue());
                            }
                            if ("desc".endsWith(entry.getKey())) {
                                r.setPrName((String) entry.getValue());
                            }
                        }
                        resourcesList.add(r);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resourcesList;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 初始化服务权限
        //initServicePermission();

        //初始化用户权限
        initUserPermission();
    }
}
