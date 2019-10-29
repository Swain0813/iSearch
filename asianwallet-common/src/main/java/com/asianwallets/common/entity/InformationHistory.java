package com.asianwallets.common.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @description:
 * @author: 沈欣然
 * @create: 2019年9月29日12:03:33
 **/
@Data
@ApiModel(value = "新闻信息历史", description = "新闻信息历史")
public class InformationHistory {

    @ApiModelProperty(value = "userId")
    private String userId;

    @ApiModelProperty(value = "新闻list")
    private List<Information> informationList;


}
