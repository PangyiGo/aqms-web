package com.osen.aqms.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * User: PangYi
 * Date: 2019-12-18
 * Time: 10:35
 * Description: 用于地图标点
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AqiRealtimeMapModel {

    private String deviceName;

    private String deviceNo;

    private String longitude;

    private String latitude;

    private int live;

    private int aqi;
}
