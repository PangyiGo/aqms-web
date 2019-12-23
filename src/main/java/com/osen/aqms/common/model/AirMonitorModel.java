package com.osen.aqms.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * User: PangYi
 * Date: 2019-12-23
 * Time: 14:21
 * Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AirMonitorModel {

    private int lv1;

    private int lv2;

    private int lv3;

    private int lv4;

    private int lv5;

    private int lv6;

    private int pm25;

    private int pm10;

    private int so2;

    private int no2;

    private int co;

    private int o3;

    private int voc;

    private List<Map<String, Object>> aqiList = new ArrayList<>(0);

    private List<AirAccordModel> airAccordModels = new ArrayList<>(0);
}
