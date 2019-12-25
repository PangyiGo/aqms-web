package com.osen.aqms.common.enums;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * User: PangYi
 * Date: 2019-12-25
 * Time: 15:56
 * Description:
 */
@AllArgsConstructor
@NoArgsConstructor
public enum TipsMessage {

    LoginSuccess("账号登录操作成功"),

    LoginFaild("账号登录操作失败"),

    LogoutSuccess("登录账号注销操作成功"),

    LogoutFaild("登录账号注销操作失败"),

    SetWarning("设备预警值设置操作"),

    AddUser("新增用户操作"),

    DeleteUser("删除用户操作"),

    RestPassword("重置密码操作"),

    DeleteDevice("删除设备操作"),

    AddDevice("新增设备操作");

    private String tips;

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }
}
