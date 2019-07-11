package com.lego.survey.user.impl.controller;

import com.lego.survey.user.impl.init.PackageInit;
import com.survey.lib.common.vo.RespVO;
import com.survey.lib.common.vo.RespVOBuilder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/package")
@Api(value = "PackageController", description = "APK管理")
public class PackageController {

    public static final Logger log = LoggerFactory.getLogger(PackageController.class);

    @Autowired
    private PackageInit packageInit;

    @ApiOperation(value = "下载apk", httpMethod = "GET", notes = "下载apk")
    @RequestMapping(value = "/downloadApp/apk", method = RequestMethod.GET)
    public void downloadApp(HttpServletResponse res){
        res.setContentType("multipart/form-data");
        res.setHeader("Content-Disposition", "attachment;fileName=survey.apk");
        try {
            byte[] datas = packageInit.getAppApk();
            res.setContentLength(datas.length);
            res.getOutputStream().write(datas);
        } catch (IOException e) {
            log.error("", e);
        }
    }

    @ApiOperation(value = "获取apk版本信息", httpMethod = "GET", notes = "获取apk版本信息")
    @RequestMapping(value = "/app/version", method = RequestMethod.GET)
    public RespVO<Map<String, Object>> version(){

        return RespVOBuilder.success(packageInit.getAppVersion());
    }

    @ApiOperation(value = "刷新apk信息", httpMethod = "GET", notes = "刷新apk信息")
    @RequestMapping(value = "/app/refresh", method = RequestMethod.GET)
    public RespVO refresh(){
        packageInit.refresh();
        return RespVOBuilder.success();
    }
}
