package com.osen.aqms.common.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * User: PangYi
 * Date: 2019Null12Null20
 * Time: 9:57
 * Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AqiReportToHourModel {

    private String deviceName;

    private String deviceNo;

    @JsonFormat(pattern = "yyyy年MM月dd日 HH时")
    private LocalDateTime dateTime;

    private String aqi = "Null";

    private String quality = "Null";

    private String level = "Null";

    private String pollute = "Null";

    private String data = "Null";

    private String tips = "Null";

    private String pm25 = "Null";

    private String pm10 = "Null";

    private String so2 = "Null";

    private String no2 = "Null";

    private String co = "Null";

    private String o3 = "Null";

    private String voc = "Null";

    private String pm25Index = "Null";

    private String pm10Index = "Null";

    private String so2Index = "Null";

    private String no2Index = "Null";

    private String coIndex = "Null";

    private String o3Index = "Null";
}
