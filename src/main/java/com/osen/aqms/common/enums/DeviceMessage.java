package com.osen.aqms.common.enums;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * User: PangYi
 * Date: 2019-12-03
 * Time: 14:52
 * Description:
 */
@NoArgsConstructor
@AllArgsConstructor
public enum DeviceMessage {
    Alarm("设备报警事件", "设备号：{}，监控因子 {} 超过正常检测限定值，请尽快处理！"),

    Warning("设备预警事件", "设备号：{}，监控因子 {} 超过设定的检测预警值，请尽快处理！"),

    Device("设备状态事件", "设备号：{}，设备状态更新为：{} 状态！");

    private String type;

    private String template;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }
}
