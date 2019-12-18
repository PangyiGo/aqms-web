package com.osen.aqms.common.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * User: PangYi
 * Date: 2019-12-18
 * Time: 11:23
 * Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AqiDataToMapModel {

    private String deviceNo;

    private String deviceName;

    private String address;

    private String installAddress;

    private String live;

    @JsonFormat(pattern = "yyyy年MM月dd日 HH:mm:ss")
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

    private int level;

    private String pollute = "-";

    private String quality = "";
}
