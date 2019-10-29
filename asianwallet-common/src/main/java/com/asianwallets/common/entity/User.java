package com.asianwallets.common.entity;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.LinkedHashMap;

/**
 * @description:
 * @author: YangXu
 * @create: 2019-09-25 14:56
 **/
@Data
@ApiModel(value = "用户表", description = "用户表")
public class User {

    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "手机号")
    private String mobile;

    @ApiModelProperty(value = "用户名")
    private String userName;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "昵称")
    private String  nickName;

    @ApiModelProperty(value = "头像")
    private String headImage;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "禁用启用")
    private Boolean enabled;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Date createTime;

    @ApiModelProperty(value = "修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    @ApiModelProperty(value = "最后一次登录时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lastLoginTime;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "用户输入的关键字和搜索次数")
    private LinkedHashMap<String, Integer> userKeyWord;
}