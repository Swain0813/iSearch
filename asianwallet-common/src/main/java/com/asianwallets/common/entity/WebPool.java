package com.asianwallets.common.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 搜索网站池表
 */
@Data
@ApiModel(value = "搜索网站池表", description = "搜索网站池表")
public class WebPool {

    @ApiModelProperty(value = "网站id")
    private String webId;

    @ApiModelProperty(value = "网站名称")
    private String webName;

    @ApiModelProperty(value = "网站链接地址")
    private String webAddress;

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
