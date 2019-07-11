package com.lego.survey.gateway.controller;

import com.survey.lib.common.consts.RespConsts;
import com.survey.lib.common.vo.RespVO;
import com.survey.lib.common.vo.RespVOBuilder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yanglf
 * @description
 * @since 2018/12/22
 **/
@RestController
public class FallbackController {


    @RequestMapping(value = "/fallback",method = RequestMethod.GET)
    public RespVO fallback(){
        return RespVOBuilder.failure(RespConsts.ERROR_SERVER_CODE,RespConsts.FAILURE);
    }
}
