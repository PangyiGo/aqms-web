package com.osen.aqms.common.enums;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * User: PangYi
 * Date: 2019-12-10
 * Time: 9:03
 * Description:
 */
@NoArgsConstructor
@AllArgsConstructor
public enum RoleType {

    SYSADMIN("ROLE_SYSADMIN", "系统超级管理员"),

    AQMSUSER("ROLE_AQMSUSER", "微型空气站普通用户"),

    AQMSAGENT("ROLE_AQMSAGENT", "微型空气站设备代理商"),

    AQMSMONITOR("ROLE_AQMSMONITOR", "微型空气站监管员"),;

    private String roleName;

    private String remark;

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
