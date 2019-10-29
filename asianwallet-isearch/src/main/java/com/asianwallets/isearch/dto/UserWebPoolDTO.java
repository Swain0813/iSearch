package com.asianwallets.isearch.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@ApiModel(value = "用户新增网站DTO", description = "用户新增网站DTO")
public class UserWebPoolDTO {

    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "网站")
    private List<AddUrlDTO> addUrlDTOs;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Date createTime;

    @ApiModelProperty(value = "创建者")
    private String creator;

    @ApiModelProperty(value = "备注")
    private String remark;
}

