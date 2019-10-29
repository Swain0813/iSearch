package com.asianwallets.isearch.dto;

import com.asianwallets.common.base.BasePageHelper;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @description:
 * @author: shenxinran
 * @create: 2019年9月29日10:44:02
 **/
@Data
@ApiModel(value = "新闻信息DTO", description = "新闻信息DTO")
public class InformationDTO extends BasePageHelper {

    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "关键字")
    private String keyWord;

    @ApiModelProperty(value = "信息id")
    private String mId;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "作者")
    private String author;

    @ApiModelProperty(value = "出处")
    private String source;

    @ApiModelProperty(value = "连接")
    private String link;

    @ApiModelProperty(value = "图片")
    private String img;

    @ApiModelProperty(value = "描述")
    private String describe;

    @ApiModelProperty(value = "发布时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Date releaseTime;

    @ApiModelProperty(value = "子类")
    private String subclass;

    @ApiModelProperty(value = "大类")
    private String categories;

    @ApiModelProperty(value = "网站")
    private String website;

    @ApiModelProperty(value = "类型")
    private String type;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Date createTime;

}
