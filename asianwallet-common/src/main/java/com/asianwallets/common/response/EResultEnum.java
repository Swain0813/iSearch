package com.asianwallets.common.response;

/**
 * @version v1.0.0
 * @classDesc: 功能描述: 业务错误码定位
 * @createTime 2018年7月2日 下午4:25:33
 * @copyright: 上海众哈网络技术有限公司
 */
public enum EResultEnum {

    SUCCESS("200"),//成功
    ERROR("50000"),//服务器繁忙，请稍后重试
    USER_IS_NOT_LOGIN("50001"),//用户未登录
    PARAMETER_IS_NOT_PRESENT("50002"),//输入参数不能为空
    REQUEST_REMOTE_ERROR("52002"),//服务内部错误
    USER_OR_PASSWORD_INCORRECT("52004"),//用户名或密码不正确
    USER_NOT_ENABLE("52005"),//用户名已禁用


    //注册登录
    MOBILE_NUMBER_IS_REGISTERED("10000"),//手机号码已注册
    MAILBOX_IS_REGISTERED("10001"),//邮箱已注册
    USER_DOES_NOT_EXIST("10002"),//用户不存在
    WRONG_PASSWORD("10003"),//密码错误
    USERNAME_HAS_BEEN_REGISTERED("10004"),//用户名已注册

    WEB_NAME_IS_EXIST("20001"),//网站名称已存在
    WEB_ADDRESS_IS_EXIST("20002"),//网站链接地址已存在
    UPLOAD_FILE_ERROR("20003"),//上传图片失败,请重试





    ;
    private String code;

    private EResultEnum(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }



}
