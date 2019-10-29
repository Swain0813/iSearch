package com.asianwallets.isearch.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "搜索网站池输入DTO", description = "搜索网站池输入DTO")
public class AddUrlDTO {

    @ApiModelProperty(value = "网站名称")
    private String webName;

    @ApiModelProperty(value = "网站链接地址")
    private String webAddress;
}
