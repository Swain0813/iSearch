package com.asianwallets.common.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * @description: 并购汪抓取配置
 * @author: XuWenQi
 * @create: 2019-10-09 11:15
 **/
@Data
@ApiModel(value = "并购汪抓取配置", description = "并购汪抓取配置")
public class Config {

    @ApiModelProperty(value = "token")
    private String token;

    @ApiModelProperty(value = "cookie")
    private String cookie;

}
