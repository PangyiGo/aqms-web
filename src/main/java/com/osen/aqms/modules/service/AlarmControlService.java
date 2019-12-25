package com.osen.aqms.modules.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.osen.aqms.common.model.AlarmControlModel;
import com.osen.aqms.modules.entity.alarm.AlarmControl;

/**
 * User: PangYi
 * Date: 2019-11-30
 * Time: 10:47
 * Description:
 */
public interface AlarmControlService extends IService<AlarmControl> {

    /**
     * 根据设备号获取空气站参数预警信息
     *
     * @param deviceNo 设备号
     * @return 信息
     */
    AlarmControlModel getAlarmControlInfo(String deviceNo);

    /**
     * 根据设备修改预警是否启动
     *
     * @param deviceNo 设备号
     * @param status   1表示启动，2表示关闭
     */
    boolean updateAlarmControlStatusToDevice(String deviceNo, String status);

    /**
     * 根据设备号修改预警值
     *
     * @param alarmControl 请求体
     * @return 信息
     */
    boolean updateAlarmControlData(AlarmControl alarmControl);

    /**
     * 添加
     *
     * @param deviceNo 预警设备号
     */
    void addAlarmControl(String deviceNo);
}
