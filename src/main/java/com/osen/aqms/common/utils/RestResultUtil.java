package com.osen.aqms.common.utils;

import com.osen.aqms.common.result.RestResult;

import static com.osen.aqms.common.enums.InfoMessage.Failed_Error;
import static com.osen.aqms.common.enums.InfoMessage.Success_OK;

/**
 * User: PangYi
 * Date: 2019-08-28
 * Time: 18:43
 * Description: 返回格式工具类
 */
@SuppressWarnings("all") //取消警告
public class RestResultUtil {

    /**
     * 异常返回
     *
     * @param code    状态码
     * @param message 异常信息
     * @return 信息
     */
    public static RestResult error(Integer code, String message) {
        return new RestResult(code, message);
    }

    /**
     * 成功请求，无数据
     *
     * @return 信息
     */
    public static RestResult success() {
        return new RestResult(Success_OK.getCode(), Success_OK.getMessage());
    }

    /**
     * 成功请求，有数据
     *
     * @param data 数据
     * @return 信息
     */
    public static RestResult success(Object data) {
        return new RestResult(Success_OK.getCode(), Success_OK.getMessage(), data);
    }

    /**
     * 成功请求，自定义消息
     *
     * @param data 数据
     * @return 信息
     */
    public static RestResult success(String message) {
        return new RestResult(Success_OK.getCode(), message);
    }

    /**
     * 失败请求，无数据
     *
     * @return 信息
     */
    public static RestResult failed() {
        return new RestResult(Failed_Error.getCode(), Failed_Error.getMessage());
    }

    /**
     * 失败请求，自定义消息
     *
     * @return 信息
     */
    public static RestResult failed(String message) {
        return new RestResult(Failed_Error.getCode(), message);
    }

    /**
     * 失败请求，有数据
     *
     * @param data 数据
     * @return 信息
     */
    public static RestResult failed(Object data) {
        return new RestResult(Failed_Error.getCode(), Failed_Error.getMessage(), data);
    }

    /**
     * 授权状态信息
     *
     * @param code    状态码
     * @param message 返回信息
     * @return 信息
     */
    public static RestResult authorization(Integer code, String message) {
        return new RestResult(code, message);
    }

}
