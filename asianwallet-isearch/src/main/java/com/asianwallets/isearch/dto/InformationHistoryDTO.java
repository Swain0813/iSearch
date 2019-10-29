package com.asianwallets.isearch.dto;

import com.asianwallets.common.entity.Information;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @description:
 * @author: 沈欣然
 * @create: 2019年9月29日12:03:33
 **/
@Data
@ApiModel(value = "新闻信息历史DTO", description = "新闻信息历史DTO")
public class InformationHistoryDTO {

    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "新闻")
    private Information information;


}
