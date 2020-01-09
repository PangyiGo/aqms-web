package com.osen.aqms.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * User: PangYi
 * Date: 2020-01-09
 * Time: 14:10
 * Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AirSensorMaxModel {

    private String pm25;

    private BigDecimal pm25Data = new BigDecimal(0);

    private String pm10;

    private BigDecimal pm10Data = new BigDecimal(0);

    private String so2;

    private BigDecimal so2Data = new BigDecimal(0);

    private String no2;

    private BigDecimal no2Data = new BigDecimal(0);

    private String o3;

    private BigDecimal o3Data = new BigDecimal(0);

    private String co;

    private BigDecimal coData = new BigDecimal(0);

    private String voc;

    private BigDecimal vocData = new BigDecimal(0);
}
