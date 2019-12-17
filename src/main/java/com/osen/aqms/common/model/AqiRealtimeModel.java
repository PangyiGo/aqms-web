package com.osen.aqms.common.model;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * User: PangYi
 * Date: 2019-12-04
 * Time: 16:14
 * Description: 实时参数+AQI的实时数据实体
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AqiRealtimeModel {

    private String deviceNo;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateTime;

    private BigDecimal pm25 = new BigDecimal(0);

    private BigDecimal pm10 = new BigDecimal(0);

    private BigDecimal so2 = new BigDecimal(0);

    private BigDecimal no2 = new BigDecimal(0);

    private BigDecimal co = new BigDecimal(0);

    private BigDecimal o3 = new BigDecimal(0);

    private BigDecimal voc = new BigDecimal(0);

    private String pm25Flag;

    private String pm10Flag;

    private String so2Flag;

    private String no2Flag;

    private String coFlag;

    private String o3Flag;

    private String vocFlag;

    private Integer airId;

    private int aqi;

    private String quality = "";

    private int level;

    private String primary = "-";

    private BigDecimal data = new BigDecimal(0);

    private String tips;
}
