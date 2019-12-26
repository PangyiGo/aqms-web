package com.osen.aqms.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * User: PangYi
 * Date: 2019-12-26
 * Time: 10:01
 * Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SensorTimeModel {

    private int max;

    private List<SensorDataModel> sensorDataModels = new ArrayList<>(0);
}
