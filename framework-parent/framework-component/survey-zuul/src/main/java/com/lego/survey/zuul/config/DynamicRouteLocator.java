/*
package com.lego.survey.zuul.config;

import com.lego.survey.zuul.repository.RoutesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.RefreshableRouteLocator;
import org.springframework.cloud.netflix.zuul.filters.SimpleRouteLocator;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;

import java.util.LinkedHashMap;
import java.util.Map;

*/
/**
 * @author yanglf
 * @description
 * @since 2019/7/15
 **//*

public class DynamicRouteLocator extends SimpleRouteLocator implements RefreshableRouteLocator {

    private  ZuulProperties properties;

    @Autowired
    private RoutesRepository routesRepository;

    public DynamicRouteLocator(String servletPath, ZuulProperties properties) {
        super(servletPath, properties);
        this.properties=properties;
    }

    @Override
    public void refresh() {
        doRefresh();
    }

    @Override
    protected Map<String, ZuulProperties.ZuulRoute> locateRoutes() {
        LinkedHashMap<String, ZuulProperties.ZuulRoute> zuulRouteLinkedHashMap=new LinkedHashMap<>();
        zuulRouteLinkedHashMap.putAll(super.locateRoutes());
        zuulRouteLinkedHashMap.putAll(routesRepository.getRoutes());

        return super.locateRoutes();
    }
}
*/
