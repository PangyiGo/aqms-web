package com.osen.aqms.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * User: PangYi
 * Date: 2019-12-23
 * Time: 10:39
 * Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AirAccordMapperModel {

    private int count;

    private BigDecimal pm25Max;

    private BigDecimal pm25Min;

    private BigDecimal pm25Avg;

    private BigDecimal pm10Max;

    private BigDecimal pm10Min;

    private BigDecimal pm10Avg;

    private BigDecimal so2Max;

    private BigDecimal so2Min;

    private BigDecimal so2Avg;

    private BigDecimal no2Max;

    private BigDecimal no2Min;

    private BigDecimal no2Avg;

    private BigDecimal coMax;

    private BigDecimal coMin;

    private BigDecimal coAvg;

    private BigDecimal o3Max;

    private BigDecimal o3Min;

    private BigDecimal o3Avg;

    private BigDecimal vocMax;

    private BigDecimal vocMin;

    private BigDecimal vocAvg;
}
