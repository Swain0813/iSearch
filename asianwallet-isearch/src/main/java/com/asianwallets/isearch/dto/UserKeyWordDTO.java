package com.asianwallets.isearch.dto;

import com.asianwallets.common.base.BasePageHelper;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 输入关键字的输入参数
 */
@Data
@ApiModel(value = "输入关键字的输入参数", description = "输入关键字的输入参数")
public class UserKeyWordDTO extends BasePageHelper{

    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "用户输入关键字")
    private String keyWord;

    @ApiModelProperty(value = "创建者")
    private String creator;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Date createTime;

    @ApiModelProperty(value = "备注")
    private String remark;
}
