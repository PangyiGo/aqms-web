package com.osen.aqms.modules.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.osen.aqms.common.model.AirQueryDataModel;
import com.osen.aqms.common.requestVo.AirQueryVo;
import com.osen.aqms.modules.entity.data.AirHour;

import java.util.List;

/**
 * User: PangYi
 * Date: 2019-11-30
 * Time: 10:47
 * Description:
 */
public interface AirHourService extends IService<AirHour> {


    /**
     * 查询设备的空气空气参数小时历史数据
     *
     * @param airQueryVo 请求体
     * @return 信息
     */
    List<AirQueryDataModel> getAirHourHistory(AirQueryVo airQueryVo);
}
