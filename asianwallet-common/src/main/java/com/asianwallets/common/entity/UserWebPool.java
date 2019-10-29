package com.asianwallets.common.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;


@Data
@ApiModel(value = "用户开通的网站", description = "用户开通的网站")
public class UserWebPool {

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "网站名称")
    private List<String> webNames;

    @ApiModelProperty(value = "创建者")
    private String creator;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Date createTime;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "禁用启用")
    private Boolean enabled;
}