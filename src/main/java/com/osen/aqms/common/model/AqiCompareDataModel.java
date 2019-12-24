package com.osen.aqms.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * User: PangYi
 * Date: 2019-12-24
 * Time: 11:21
 * Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AqiCompareDataModel {

    private List<AqiHistoryToHour> firstDeviceData = new ArrayList<>(0);

    private List<AqiHistoryToHour> secondDeviceData = new ArrayList<>(0);
}
