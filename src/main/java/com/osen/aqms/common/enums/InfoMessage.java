package com.osen.aqms.common.enums;

import com.osen.aqms.common.utils.ConstUtil;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * User: PangYi
 * Date: 2019-08-28
 * Time: 17:46
 * Description: 统一信息体
 */
@NoArgsConstructor
@AllArgsConstructor
public enum InfoMessage {

    /**
     * 异常信息体
     */
    UnknownSystem_Error(ConstUtil.UNOK, "系统未知异常"),

    /**
     * 成功信息体
     */
    Success_OK(ConstUtil.OK, "请求成功"),

    Failed_Error(ConstUtil.UNOK, "请求失败"),

    /**
     * 提示信息体
     */
    User_Need_Authorization(ConstUtil.UNOK, "Account not logged in"),

    User_Login_Failed(ConstUtil.UNOK, "账号或密码输入错误！"),

    User_Login_Success(ConstUtil.OK, "登录成功！"),

    User_NO_Access(ConstUtil.UNOK, "用户无权访问！"),

    User_Logout_Success(ConstUtil.OK, "登录账号已成功注销！"),

    User_Login_Guoqi(ConstUtil.UNOK, "登录状态过期，请重新登录！");

    private Integer code;

    private String message;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
