package com.osen.aqms.common.enums;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * User: PangYi
 * Date: 2019-11-29
 * Time: 10:49
 * Description: 空气站设备传感器参数
 */
@NoArgsConstructor
@AllArgsConstructor
public enum AirSensor {

    PM25("a34004", "PM25", "PM2.5"),

    PM10("a34002", "PM10", "PM10"),

    SO2("a21026", "SO2", "SO2"),

    NO2("a21004", "NO2", "NO2"),

    O3("O3", "O3", "O3"),

    CO("a21005", "CO", "CO"),

    VOC("VOC", "VOC", "TVOC");

    // 国标
    private String code;

    // 自定义名称
    private String name;

    // 参数描述
    private String desc;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
