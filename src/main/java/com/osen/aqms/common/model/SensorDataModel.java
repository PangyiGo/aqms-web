package com.osen.aqms.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * User: PangYi
 * Date: 2019-12-26
 * Time: 10:01
 * Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SensorDataModel {

    private String lng;

    private String lat;

    private int count;
}
