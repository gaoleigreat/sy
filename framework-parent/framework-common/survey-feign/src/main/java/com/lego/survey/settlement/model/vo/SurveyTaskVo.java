package com.lego.survey.settlement.model.vo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lego.survey.settlement.model.entity.SurveyOriginal;
import com.lego.survey.settlement.model.entity.SurveyTask;
import com.survey.lib.common.utils.SnowflakeIdUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author yanglf
 * @description
 * @since 2019/1/14
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class SurveyTaskVo {
    @ApiModelProperty("任务ID")
    private Long id;
    @NotBlank(message = "任务名称不能为空")
    @ApiModelProperty("任务名称")
    private String name;
    @NotBlank(message = "仪器品牌不能为空")
    @ApiModelProperty("仪器品牌")
    private String deviceBrand;
    @NotBlank(message = "仪器型号不能为空")
    @ApiModelProperty("仪器型号")
    private String deviceModel;
    @ApiModelProperty("温度，摄氏度")
    private String temperature;
    @ApiModelProperty("气压")
    private String pressure;
    @ApiModelProperty("天气")
    @NotBlank(message = "天气不能为空")
    private String weather;
    @ApiModelProperty("测量类型 0: 单次测量，1：后前前后， 2：前后后前， 3：交替")
    private String measureType;
    @ApiModelProperty("创建时间")
    private Date createTime;
    @ApiModelProperty("任务结束时间")
    private Date endTime;
    @ApiModelProperty("上传时间")
    private Date uploadTime;
    @ApiModelProperty("原始数据总数")
    private int totalNum;
    @ApiModelProperty("工区code")
    private String workspaceCode;
    @ApiModelProperty("原始数据")
    private List<SurveyOriginalVo> originalData;


    @JsonIgnore
    public SurveyTask getSurveyTask() {
        Date currentDate=new Date();
        return SurveyTask.builder()
                .createTime(currentDate)
                .endTime(this.endTime)
                .id(this.id)
                .name(this.name)
                .uploadTime(currentDate)
                .property(getProperty(this.deviceBrand, this.deviceModel, this.temperature, this.pressure, this.weather, this.totalNum))
                // "BF", "BFFB", "FBBF", "BF..."
                .measureType(getMeasureTypeInteger(this.measureType))
                .build();
    }

    private Integer getMeasureTypeInteger(String measureType) {
        if ("BF".equals(measureType)) {
            return 0;
        } else if ("BFFB".equals(measureType)) {
            return 1;
        } else if ("FBBF".equals(measureType)) {
            return 2;
        } else if ("BF...".equals(measureType)) {
            return 3;
        }
        return -1;
    }


    private String getMeasureTypeStr(Integer measureType) {
        if (measureType==1) {
            return "BF";
        } else if (measureType==2) {
            return "BFFB";
        } else if (measureType==3) {
            return "FBBF";
        } else if (measureType==4) {
            return "BF...";
        }
        return null;
    }


    @JsonIgnore
    private String getProperty(String deviceBrand, String deviceModel, String temperature, String pressure, String weather, int totalNum) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("deviceBrand", deviceBrand);
        jsonObject.put("deviceModel", deviceModel);
        jsonObject.put("temperature", temperature);
        jsonObject.put("pressure", pressure);
        //{"晴", "风雪", "雨"};
        jsonObject.put("weather", weather);
        jsonObject.put("totalNum", totalNum);
        return jsonObject.toJSONString();
    }


    @JsonIgnore
    public List<SurveyOriginal> getSurveyOriginal() {
        Date currentDate=new Date();
        List<SurveyOriginal> surveyOriginals = new ArrayList<>();
        List<SurveyOriginalVo> originalVos = this.getOriginalData();
        if (originalVos != null) {
            originalVos.forEach(originalVo -> {
                SurveyOriginal surveyOriginal = SurveyOriginal.builder()
                        .bpCode(originalVo.getBpCode())
                        .bpType(originalVo.getBpType())
                        .fpCode(originalVo.getFpCode())
                        .fpType(originalVo.getFpType())
                        .id(SnowflakeIdUtils.createId())
                        .taskId(this.getId())
                        .uploadTime(currentDate)
                        .index(originalVo.getIndex())
                        .datas(JSONObject.toJSONString(originalVo.getDatas()))
                        .build();
                surveyOriginals.add(surveyOriginal);
            });
        }
        return surveyOriginals;
    }

    @JsonIgnore
    public SurveyOriginalVo loadSurveyOriginalVo(SurveyOriginal surveyOriginal) {
        return SurveyOriginalVo.builder()
                .bpCode(surveyOriginal.getBpCode())
                .bpType(surveyOriginal.getBpType())
                .id(surveyOriginal.getId())
                .fpCode(surveyOriginal.getFpCode())
                .fpType(surveyOriginal.getFpType())
                .datas(surveyOriginal.getDatas())
                /*.datas(getOriginalArray(surveyOriginal))*/
                .build();

    }

    @JsonIgnore
    public SurveyTaskVo loadSurveyTaskVo(SurveyTask surveyTask) {
        return SurveyTaskVo.builder()
                .createTime(surveyTask.getCreateTime())
                .endTime(surveyTask.getEndTime())
                .id(surveyTask.getId())
                .deviceBrand((String) parseProperty(surveyTask.getProperty(), "deviceBrand"))
                .deviceModel((String) parseProperty(surveyTask.getProperty(), "deviceModel"))
                .temperature(parseProperty(surveyTask.getProperty(), "temperature") + "")
                .pressure((parseProperty(surveyTask.getProperty(), "pressure") + ""))
                .weather((String) parseProperty(surveyTask.getProperty(), "weather"))
                .totalNum((Integer) parseProperty(surveyTask.getProperty(), "totalNum"))
                .measureType(getMeasureTypeStr(surveyTask.getMeasureType()))
                .build();
    }



    @JsonIgnore
    private Object parseProperty(String jsonObj, String key) {
        JSONObject jsonObject = JSON.parseObject(jsonObj);
        return jsonObject.get(key);
    }


    @JsonIgnore
    private List<OriginalDataVo> getOriginalArray(SurveyOriginal surveyOriginal) {
        String data = surveyOriginal.getDatas();
        return JSONObject.parseArray(data, OriginalDataVo.class);
    }


}
