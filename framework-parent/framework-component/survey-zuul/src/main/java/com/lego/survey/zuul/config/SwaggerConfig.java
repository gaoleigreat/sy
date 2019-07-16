package com.lego.survey.zuul.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yanglf
 * @description
 * @since 2019/3/6
 **/
@Component
@EnableSwagger2
public class SwaggerConfig {

    @Autowired
    private ZuulProperties properties;

    @Bean
    @Primary
    public SwaggerResourcesProvider swaggerResourcesProvider() {
        return () ->{
            List<SwaggerResource> resources = new ArrayList<>();
           /* properties.getRoutes().values()
                    .forEach(route -> resources
                            .add(createResource(
                                    route.getId(),route.getPath().replace("/**",""))));*/

            resources.add(createResource("工程服务","api-project"));
            resources.add(createResource("权限服务","api-auth"));
            resources.add(createResource("文件服务","api-file"));
            resources.add(createResource("报表服务","api-report"));
            resources.add(createResource("用户服务","api-user"));
            resources.add(createResource("沉降服务","api-settlement"));

            return  resources;
        };
    }

    private SwaggerResource createResource(String name, String location) {
        SwaggerResource swaggerResource = new SwaggerResource();
        swaggerResource.setName(name);
        swaggerResource.setLocation("/"+location+"/v2/api-docs");
        swaggerResource.setSwaggerVersion("2.0");
        return swaggerResource;
    }


}
