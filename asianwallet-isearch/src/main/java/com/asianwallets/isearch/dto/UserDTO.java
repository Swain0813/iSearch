package com.asianwallets.isearch.dto;
import com.asianwallets.common.base.BasePageHelper;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @description:
 * @author: YangXu
 * @create: 2019-09-25 15:45
 **/
@Data
@ApiModel(value = "用户DTO", description = "用户DTO")
public class UserDTO extends BasePageHelper {

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

    @ApiModelProperty(value = "备注")
    private String remark;
}
