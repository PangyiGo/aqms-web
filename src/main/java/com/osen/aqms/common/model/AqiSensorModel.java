package com.osen.aqms.common.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * User: PangYi
 * Date: 2020-01-02
 * Time: 17:29
 * Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AqiSensorModel {

    private BigDecimal number = new BigDecimal(0);

    @JsonFormat(pattern = "MM/dd HH")
    private LocalDateTime dateTime;

}
