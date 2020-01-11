package com.osen.aqms.modules.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.osen.aqms.common.model.AqiDataToMapModel;
import com.osen.aqms.common.model.AqiViewModel;
import com.osen.aqms.common.requestVo.AddressVo;
import com.osen.aqms.common.requestVo.AirQueryVo;
import com.osen.aqms.modules.entity.alarm.AirAlarm;

import java.util.List;

/**
 * User: PangYi
 * Date: 2019-11-30
 * Time: 10:47
 * Description:
 */
public interface AirAlarmService extends IService<AirAlarm> {

    /**
     * 根据区域查询设备列表的空气站实时数据
     *
     * @param addressVo 可指定区域，或默认查询全部
     * @return 信息
     */
    List<AqiDataToMapModel> getAirAlarmRealtime(AddressVo addressVo);

    /**
     * 根据设备号获取报警历史数据
     *
     * @param airQueryVo 请求体
     * @return 信息
     */
    List<AirAlarm> getAirAlarmHistory(AirQueryVo airQueryVo);

    /**
     * 根据设备号获取当月报警次数
     *
     * @param deviceNo 设备号
     * @return 信息
     */
    AqiViewModel getAlarmNumber(String deviceNo);
}
