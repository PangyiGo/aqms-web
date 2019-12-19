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
 * Time: 16:11
 * Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AqiHistoryToDay {

    private String deviceNo;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime dateTime;

    private BigDecimal pm25 = new BigDecimal(0);

    private BigDecimal pm10 = new BigDecimal(0);

    private BigDecimal so2 = new BigDecimal(0);

    private BigDecimal no2 = new BigDecimal(0);

    private BigDecimal co = new BigDecimal(0);

    private BigDecimal o3 = new BigDecimal(0);

    private BigDecimal voc = new BigDecimal(0);

    private int level;

    private int aqi;

    private String pollute = "-";

    private String quality = "";
}
