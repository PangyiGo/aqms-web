package com.osen.aqms.common.model;

import lombok.Data;

import java.math.BigDecimal;

/**
 * User: PangYi
 * Date: 2020-01-03
 * Time: 9:44
 * Description:
 */
@Data
public class AqiSensorRankModel {

    private String deviceNo;

    private String deviceName;

    private BigDecimal number = new BigDecimal(0);
}
