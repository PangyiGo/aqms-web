package com.osen.aqms.common.model;

import com.osen.aqms.modules.entity.system.Device;
import lombok.Data;

import java.util.List;

/**
 * User: PangYi
 * Date: 2019-12-17
 * Time: 15:48
 * Description:
 */
@Data
public class AirRealTimeModel {

    private AqiRealtimeModel aqiRealtimeModel;

    private Device device;

    private List<AirDataModel> dataModels;
}
