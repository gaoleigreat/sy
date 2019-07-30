package com.lego.survey.gateway.provider;
import lombok.AllArgsConstructor;
import org.springframework.cloud.gateway.config.GatewayProperties;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.support.NameUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yanglf
 * @description
 * @since 2019/7/15
 **/
@Component
@Primary
@AllArgsConstructor
public class GatewaySwaggerProvider implements SwaggerResourcesProvider {

    public static final String API_URL = "/v2/api-docs";

    private final RouteLocator routeLocator;

    private final GatewayProperties gatewayProperties;

    @Override
    public List<SwaggerResource> get() {
        List<SwaggerResource> swaggerResources = new ArrayList<>();
        List<String> routes = new ArrayList<>();

        routeLocator.getRoutes().subscribe(r -> routes.add(r.getId()));
       /* gatewayProperties.getRoutes().stream().filter(g -> routes.contains(g.getId()))
                .forEach(g -> g.getPredicates().stream()
                        .filter(p -> "Path".equalsIgnoreCase(p.getName()))
                        .forEach(p -> swaggerResources.add(swaggerResource(g.getId(), p.getArgs().get(NameUtils.GENERATED_NAME_PREFIX + "0")
                                .replace("/**", API_URL)
                        ))));*/
        swaggerResources.add(swaggerResource("用户服务","/api-user/v2/api-docs")) ;
        swaggerResources.add(swaggerResource("工程服务","/api-project/v2/api-docs")) ;
        swaggerResources.add(swaggerResource("文件服务","/api-file/v2/api-docs")) ;
        swaggerResources.add(swaggerResource("沉降服务","/api-settlement/v2/api-docs")) ;
        swaggerResources.add(swaggerResource("报表服务","/api-report/v2/api-docs")) ;
        swaggerResources.add(swaggerResource("权限服务","/api-auth/v2/api-docs")) ;
        return swaggerResources;
    }


    private SwaggerResource swaggerResource(String name, String location) {
        SwaggerResource swaggerResource = new SwaggerResource();
        swaggerResource.setLocation(location);
        swaggerResource.setName(name);
        swaggerResource.setSwaggerVersion("2.0");
        return swaggerResource;
    }

}
