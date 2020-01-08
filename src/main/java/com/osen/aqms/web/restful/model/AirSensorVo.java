package com.osen.aqms.web.restful.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AirSensorVo {

    // 设备号
    private String deviceNo;

    // 校准参数值
    private String number;

    // 参数
    private String sensor;
}
