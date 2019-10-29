package com.asianwallets.isearch.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;


@Data
@ApiModel(value = "并购汪抓取配置输入DTO", description = "并购汪抓取配置输入DTO")
public class ConfigDTO{

    @ApiModelProperty(value = "id")
    private String configId;

    @ApiModelProperty(value = "token")
    private String token;

    @ApiModelProperty(value = "cookie")
    private String cookie;

    @ApiModelProperty(value = "remark：1：并购汪 2：雪球")
    private String remark;
}
