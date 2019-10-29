package com.asianwallets.common.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 关键字记录表
 */
@Data
@ApiModel(value = "关键字记录表", description = "关键字记录表")
public class UserKeyWord {

    @ApiModelProperty(value = "关键字id")
    private String id;

    @ApiModelProperty(value = "输入关键字")
    private String keyWord;

    @ApiModelProperty(value = "关键字搜索次数")
    private Integer searchNumber;

    @ApiModelProperty(value = "创建者")
    private String creator;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Date createTime;

    @ApiModelProperty(value = "备注")
    private String remark;

}
