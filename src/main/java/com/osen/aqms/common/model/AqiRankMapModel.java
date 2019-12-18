package com.osen.aqms.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * User: PangYi
 * Date: 2019-12-18
 * Time: 14:17
 * Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AqiRankMapModel {

    private AqiDataToMapModel mapModel;

    private List<AqiRealtimeMapModel> mapModels;
}
