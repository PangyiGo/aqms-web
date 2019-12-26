package com.osen.aqms.common.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * User: PangYi
 * Date: 2019-12-26
 * Time: 10:59
 * Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PolluteMapResultModel {

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateTime;

    private SensorTimeModel sensorTimeModel;
}
