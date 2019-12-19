package com.osen.aqms.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * User: PangYi
 * Date: 2019-12-19
 * Time: 10:05
 * Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AirAvgModel {

    private BigDecimal pm25Avg;

    private BigDecimal pm10Avg;

    private BigDecimal so2Avg;

    private BigDecimal no2Avg;

    private BigDecimal coAvg;

    private BigDecimal o3Avg;

    private BigDecimal vocAvg;
}
