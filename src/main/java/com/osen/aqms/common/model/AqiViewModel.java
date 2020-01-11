package com.osen.aqms.common.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * User: PangYi
 * Date: 2020-01-11
 * Time: 8:45
 * Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AqiViewModel {

    private int alarmNumber;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime alarmDate;

    private String alarmSensor;

    private int perfect;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime badDay;

    private String topSensor;
}
