package com.asianwallets.isearch.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @description:
 * @author: shenxinran
 * @create: 时间
 **/
@Data
@ApiModel(value = "删除新闻信息DTO", description = "删除新闻信息DTO")
public class DeleteInformationDTO {

    @ApiModelProperty(value = "用户id")
    private String userId;


    @ApiModelProperty(value = "mIds")
    private List<String> mIds;
}
