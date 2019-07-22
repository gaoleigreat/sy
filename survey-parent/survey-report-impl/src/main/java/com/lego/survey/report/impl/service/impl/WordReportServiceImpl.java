package com.lego.survey.report.impl.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lego.survey.report.impl.service.IWordReportService;
import com.lego.survey.settlement.model.vo.SurveyReportVo;
import com.lego.survey.user.feign.UserClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
@Service
public class WordReportServiceImpl implements IWordReportService {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private UserClient userClient;

    @Autowired
    private HttpServletRequest httpServletRequest;


    @Override
    public SurveyReportVo getSurveyReportVo(String workspaceCode) {
        SurveyReportVo surveyReportVo = new SurveyReportVo();

        //用来封装所有条件的对象
        Query query = new Query();
        //用来构建条件
        Criteria criteria = new Criteria();
        criteria.and("workSpace").elemMatch(new Criteria().and("code").is(workspaceCode));
        query.addCriteria(criteria);
        JSONObject jsonObject = mongoTemplate.findOne(query, JSONObject.class, "section");
        // 标段名
        surveyReportVo.setTitle(jsonObject.getString("name"));

        //设置地址
        JSONArray jsonArray = jsonObject.getJSONArray("workSpace");
        for (int i = 0; i < jsonArray.size(); i++) {
            if (jsonArray.getJSONObject(i).getString("code").equals(workspaceCode)) {
                surveyReportVo.setAddress(jsonArray.getJSONObject(i).getString("name"));
            }
        }

        //设置制造员
      /*  String token = httpServletRequest.getHeader("token");

        String userName = userClient.queryUserByToken(token).getInfo().getName();
        surveyReportVo.setMaker(userName);*/
        return surveyReportVo;
    }
}
