package com.osen.aqms.common.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * User: PangYi
 * Date: 2019-12-20
 * Time: 9:57
 * Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AqiReportToDayModel {

    private String deviceName;

    private String deviceNo;

    @JsonFormat(pattern = "yyyy年MM月dd日")
    private LocalDateTime dateTime;

    private String aqi = "NA";

    private String quality = "NA";

    private String level = "NA";

    private String pollute = "NA";

    private String data = "NA";

    private String tips = "NA";

    private String pm25 = "NA";

    private String pm10 = "NA";

    private String so2 = "NA";

    private String no2 = "NA";

    private String co = "NA";

    private String o3 = "NA";

    private String voc = "NA";

    private String pm25Index = "NA";

    private String pm10Index = "NA";

    private String so2Index = "NA";

    private String no2Index = "NA";

    private String coIndex = "NA";

    private String o3Index = "NA";
}
