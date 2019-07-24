package com.lego.survey.settlement.model.vo;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lego.survey.settlement.model.entity.SurveyOriginal;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.StringEscapeUtils;

import javax.validation.constraints.NotBlank;
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
public class SurveyOriginalVo {
    @ApiModelProperty("原始数据id")
    private Long id;
    @ApiModelProperty("原始数据任务id")
    private Long taskId;
    @ApiModelProperty("后视点编码")
    @NotBlank(message = "后视点编码不能为空")
    private String bpCode;
    @ApiModelProperty("后视点类型 0 基准点 1 观测点  2 转点")
    private Integer bpType;
    @NotBlank(message = "前视点编码不能为空")
    @ApiModelProperty("前视点编码")
    private String fpCode;
    @ApiModelProperty("前视点类型 0 基准点 1 观测点  2 转点")
    private Integer fpType;
    @JsonIgnore
    @ApiModelProperty("原始测量数据 JSON数组")
    private List<OriginalDataVo> surveyDatas;
    @ApiModelProperty("原始测量数据 JSON数组")
    private String datas;
    @ApiModelProperty("任务中索引值")
    private Integer index;


    @JsonIgnore
    public SurveyOriginal getSurveyOriginal() {
        return SurveyOriginal.builder()
                .bpCode(this.bpCode)
                .bpType(this.bpType)
                .fpCode(this.fpCode)
                .fpType(this.fpType)
                .id(this.id)
                .index(this.index)
                .taskId(this.taskId)
                .uploadTime(new Date())
                .datas(StringEscapeUtils.unescapeJava(this.datas))
                .build();
    }


    @JsonIgnore
    public SurveyOriginalVo loadSurveyOriginalVo(SurveyOriginal surveyOriginal) {
        return SurveyOriginalVo.builder()
                .bpCode(surveyOriginal.getBpCode())
                .bpType(surveyOriginal.getBpType())
                .fpCode(surveyOriginal.getFpCode())
                .fpType(surveyOriginal.getFpType())
                .id(surveyOriginal.getId())
                .index(surveyOriginal.getIndex())
                .taskId(surveyOriginal.getTaskId())
                .datas(surveyOriginal.getDatas())
             //   .surveyDatas(JSONObject.parseArray(StringEscapeUtils.unescapeJava(surveyOriginal.getDatas()), OriginalDataVo.class))
                        //.datas(JSONObject.parseArray(surveyOriginal.getDatas(),OriginalDataVo.class))
                 .build();
    }


}
