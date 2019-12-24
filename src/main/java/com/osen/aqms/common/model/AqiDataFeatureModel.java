package com.osen.aqms.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * User: PangYi
 * Date: 2019-12-23
 * Time: 19:07
 * Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AqiDataFeatureModel {

    private AqiFeatureModel aqiFeatureModel = new AqiFeatureModel();

    private List<AqiAvgModel> nowAqiAvgModels = new ArrayList<>(0);

    private List<AqiAvgModel> yeaAqiAvgModels = new ArrayList<>(0);

    private List<AqiAvgModel> monAqiAvgModels = new ArrayList<>(0);
}
