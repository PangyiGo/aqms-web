package com.osen.aqms.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * User: PangYi
 * Date: 2019-12-23
 * Time: 9:30
 * Description: 数据统计
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AirAccordModel {

    private String deviceNo;

    private String deviceName;

    private String aqi;

    private String level;

    private String pollute;

    private int count;

    private BigDecimal pm25Max = new BigDecimal(0);

    private BigDecimal pm25Min = new BigDecimal(0);

    private BigDecimal pm25Avg = new BigDecimal(0);

    private BigDecimal pm10Max = new BigDecimal(0);

    private BigDecimal pm10Min = new BigDecimal(0);

    private BigDecimal pm10Avg = new BigDecimal(0);

    private BigDecimal so2Max = new BigDecimal(0);

    private BigDecimal so2Min = new BigDecimal(0);

    private BigDecimal so2Avg = new BigDecimal(0);

    private BigDecimal no2Max = new BigDecimal(0);

    private BigDecimal no2Min = new BigDecimal(0);

    private BigDecimal no2Avg = new BigDecimal(0);

    private BigDecimal coMax = new BigDecimal(0);

    private BigDecimal coMin = new BigDecimal(0);

    private BigDecimal coAvg = new BigDecimal(0);

    private BigDecimal o3Max = new BigDecimal(0);

    private BigDecimal o3Min = new BigDecimal(0);

    private BigDecimal o3Avg = new BigDecimal(0);

    private BigDecimal vocMax = new BigDecimal(0);

    private BigDecimal vocMin = new BigDecimal(0);

    private BigDecimal vocAvg = new BigDecimal(0);
}
