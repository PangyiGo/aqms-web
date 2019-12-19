package com.osen.aqms.common.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * User: PangYi
 * Date: 2019-12-19
 * Time: 14:04
 * Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AirQueryDataModel {

    private String deviceNo;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateTime;

    private BigDecimal pm25;

    private BigDecimal pm10;

    private BigDecimal so2;

    private BigDecimal no2;

    private BigDecimal co;

    private BigDecimal o3;

    private BigDecimal voc;
}
