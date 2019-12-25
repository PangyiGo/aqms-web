package com.osen.aqms.modules.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.osen.aqms.common.model.AirQueryDataModel;
import com.osen.aqms.common.model.AlarmControlModel;
import com.osen.aqms.common.model.AqiRealtimeModel;
import com.osen.aqms.common.utils.RedisOpsUtil;
import com.osen.aqms.common.utils.TableNameUtil;
import com.osen.aqms.modules.entity.alarm.AlarmControl;
import com.osen.aqms.modules.mapper.alarm.AlarmControlMapper;
import com.osen.aqms.modules.service.AlarmControlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * User: PangYi
 * Date: 2019-11-30
 * Time: 10:52
 * Description:
 */
@Service
public class AlarmControlServiceImpl extends ServiceImpl<AlarmControlMapper, AlarmControl> implements AlarmControlService {

    @Autowired
    private RedisOpsUtil redisOpsUtil;

    @Override
    public AlarmControlModel getAlarmControlInfo(String deviceNo) {
        AlarmControlModel alarmControlModel = new AlarmControlModel();
        AirQueryDataModel airQueryDataModel = new AirQueryDataModel();
        // 获取设备当前实时数据信息
        String dataJson = redisOpsUtil.getToMap(TableNameUtil.Air_Realtime, deviceNo);
        if (dataJson != null) {
            AqiRealtimeModel aqiRealtimeModel = JSON.parseObject(dataJson, AqiRealtimeModel.class);
            airQueryDataModel.setDeviceNo(aqiRealtimeModel.getDeviceNo());
            airQueryDataModel.setDateTime(aqiRealtimeModel.getDateTime());
            airQueryDataModel.setPm25(aqiRealtimeModel.getPm25());
            airQueryDataModel.setPm10(aqiRealtimeModel.getPm10());
            airQueryDataModel.setSo2(aqiRealtimeModel.getSo2());
            airQueryDataModel.setNo2(aqiRealtimeModel.getNo2());
            airQueryDataModel.setCo(aqiRealtimeModel.getCo());
            airQueryDataModel.setO3(aqiRealtimeModel.getO3());
            airQueryDataModel.setVoc(aqiRealtimeModel.getVoc());
        }
        alarmControlModel.setAirQueryDataModel(airQueryDataModel);

        LambdaQueryWrapper<AlarmControl> wrapper = Wrappers.<AlarmControl>lambdaQuery().eq(AlarmControl::getDeviceNo, deviceNo);
        try {
            AlarmControl alarmControl = super.getOne(wrapper, true);
            alarmControlModel.setAlarmControl(alarmControl == null ? new AlarmControl() : alarmControl);
        } catch (Exception e) {
            return alarmControlModel;
        }
        return alarmControlModel;
    }

    @Override
    public boolean updateAlarmControlStatusToDevice(String deviceNo, String status) {
        // 状态
        Integer state = Integer.valueOf(status);
        LambdaUpdateWrapper<AlarmControl> wrapper = Wrappers.<AlarmControl>lambdaUpdate().set(AlarmControl::getStatus, state).eq(AlarmControl::getDeviceNo, deviceNo);
        return super.update(wrapper);
    }

    @Override
    public boolean updateAlarmControlData(AlarmControl alarmControl) {
        LambdaUpdateWrapper<AlarmControl> wrapper = Wrappers.<AlarmControl>lambdaUpdate();
        wrapper.eq(AlarmControl::getDeviceNo, alarmControl.getDeviceNo());
        alarmControl.setUpdateTime(LocalDateTime.now());
        return super.update(alarmControl, wrapper);
    }

    @Override
    public void addAlarmControl(String deviceNo) {
        AlarmControl alarmControl = new AlarmControl();
        alarmControl.setDeviceNo(deviceNo);
        alarmControl.setPm25(new BigDecimal(0));
        alarmControl.setPm10(new BigDecimal(0));
        alarmControl.setSo2(new BigDecimal(0));
        alarmControl.setNo2(new BigDecimal(0));
        alarmControl.setCo(new BigDecimal(0));
        alarmControl.setO3(new BigDecimal(0));
        alarmControl.setVoc(new BigDecimal(0));
        alarmControl.setCreateTime(LocalDateTime.now());
        alarmControl.setUpdateTime(LocalDateTime.now());
        super.save(alarmControl);
    }
}
