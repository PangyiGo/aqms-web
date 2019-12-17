package com.osen.aqms.modules.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.osen.aqms.common.model.AirDataModel;
import com.osen.aqms.modules.entity.data.AirHistory;

import java.util.List;

/**
 * User: PangYi
 * Date: 2019-11-30
 * Time: 10:47
 * Description:
 */
public interface AirHistoryService extends IService<AirHistory> {

    /**
     * 获取指定监控参数因子近12小时的历史数据
     *
     * @param deviceNo 设备号
     * @param sensor   参数因子
     * @return 信息
     */
    List<AirDataModel> getAirDataToSensor(String deviceNo, String sensor);
}
