package com.lego.survey.zuul.config;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yanglf
 * @description
 * @since 2019/3/6
 **/
@Component
@Primary
public class DocumentationConfig implements SwaggerResourcesProvider {
    @Override
    public List<SwaggerResource> get() {
        List<SwaggerResource> resources = new ArrayList<>();
        resources.add(swaggerResource("工程服务", "/api-project/v2/api-docs", "2.0"));
        resources.add(swaggerResource("沉降服务", "/api-settlement/v2/api-docs", "2.0"));
        resources.add(swaggerResource("用户服务","/api-user/v2/api-docs","2.0"));
        resources.add(swaggerResource("文件服务","/api-file/v2/api-docs","2.0"));
        resources.add(swaggerResource("权限服务","/api-auth/v2/api-docs","2.0"));
        resources.add(swaggerResource("报表服务","/api-report/v2/api-docs","2.0"));


        return resources;
    }

    private SwaggerResource swaggerResource(String name, String location, String version) {
        SwaggerResource swaggerResource = new SwaggerResource();
        swaggerResource.setName(name);
        swaggerResource.setLocation(location);
        swaggerResource.setSwaggerVersion(version);
        return swaggerResource;
    }


}
