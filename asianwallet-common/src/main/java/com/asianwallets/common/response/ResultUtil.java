package com.asianwallets.common.response;
import lombok.extern.slf4j.Slf4j;

/**
 * @version v1.0.0
 * @classDesc: 功能描述: 返回值工具类
 * @createTime 2018年7月2日 下午4:12:38
 * @copyright: 上海众哈网络技术有限公司
 */
@Slf4j
public class ResultUtil {

    /**
     * @param object
     * @methodDesc: 功能描述: 操作成功返回信息
     * @author Wu, Hua-Zheng
     * @createTime 2018年7月2日 下午4:13:40
     * @version v1.0.0
     */
    public static BaseResponse success(Object object) {
        BaseResponse response = new BaseResponse();
        response.setCode(EResultEnum.SUCCESS.getCode());
        response.setMsg("SUCCESS");
        response.setData(object);
        return response;
    }

    /**
     * @methodDesc: 功能描述: 操作成功不返回消息
     * @author Wu, Hua-Zheng
     * @createTime 2018年7月2日 下午4:13:40
     * @version v1.0.0
     */
    public static BaseResponse success() {
        return ResultUtil.success(null);
    }

    /**
     * @param code
     * @param msg
     * @methodDesc: 功能描述: 操作失败返回的消息
     * @author Wu, Hua-Zheng
     * @createTime 2018年7月2日 下午4:14:42
     * @version v1.0.0
     */
    public static BaseResponse error(String code, String msg) {
        BaseResponse response = new BaseResponse();
        response.setCode(code);
        response.setMsg(msg);
        return response;
    }
    public static BaseResponse error(String code, String msg,Object object) {
        BaseResponse response = new BaseResponse();
        response.setCode(code);
        response.setMsg(msg);
        response.setData(object);
        return response;
    }
}
