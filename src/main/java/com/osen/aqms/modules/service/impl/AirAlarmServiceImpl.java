package com.osen.aqms.modules.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.osen.aqms.common.config.MybatisPlusConfig;
import com.osen.aqms.common.model.AqiDataToMapModel;
import com.osen.aqms.common.requestVo.AddressVo;
import com.osen.aqms.common.requestVo.AirQueryVo;
import com.osen.aqms.common.utils.*;
import com.osen.aqms.modules.entity.alarm.AirAlarm;
import com.osen.aqms.modules.entity.system.Device;
import com.osen.aqms.modules.mapper.alarm.AirAlarmMapper;
import com.osen.aqms.modules.service.AirAlarmService;
import com.osen.aqms.modules.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * User: PangYi
 * Date: 2019-11-30
 * Time: 10:52
 * Description:
 */
@Service
public class AirAlarmServiceImpl extends ServiceImpl<AirAlarmMapper, AirAlarm> implements AirAlarmService {

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private RedisOpsUtil redisOpsUtil;

    @Override
    public List<AqiDataToMapModel> getAirAlarmRealtime(AddressVo addressVo) {
        List<AqiDataToMapModel> aqiDataToMapModels = new ArrayList<>(0);
        List<Device> deviceList;
        if (addressVo == null) {
            // 查询全部设备列表数据
            String username = SecurityUtil.getUsername();
            deviceList = deviceService.findDeviceAllToUsername(username);
        } else {
            // 查询
            String address = addressVo.getAddress();
            String level = addressVo.getLevel();
            deviceList = deviceService.findDeviceGroupByAddress(address, level);
        }
        for (Device device : deviceList) {
            AqiDataToMapModel aqiDataToMapModel = new AqiDataToMapModel();
            // 获取实时数据
            String dataJson = redisOpsUtil.getToMap(TableNameUtil.Air_Alarm, device.getDeviceNo());
            if (dataJson != null) {
                AirAlarm airAlarm = JSON.parseObject(dataJson, AirAlarm.class);
                BeanUtil.copyProperties(airAlarm, aqiDataToMapModel);
                aqiDataToMapModels.add(aqiDataToMapModel);
            } else {
                continue;
            }
            aqiDataToMapModel.setDeviceName(device.getDeviceName());
            aqiDataToMapModel.setDeviceNo(device.getDeviceNo());
            String ade = (StrUtil.isNotEmpty(device.getProvince()) ? device.getProvince() : "") + (StrUtil.isNotEmpty(device.getCity()) ? device.getCity() : "") + (StrUtil.isNotEmpty(device.getArea()) ? device.getArea() : "");
            aqiDataToMapModel.setAddress(ade);
            aqiDataToMapModel.setInstallAddress((StrUtil.isNotEmpty(device.getAddress()) ? device.getAddress() : ""));
            aqiDataToMapModel.setLive(device.getLive() == ConstUtil.OPEN_STATUS ? "在线" : "离线");
            aqiDataToMapModel.setLongitude(device.getLongitude());
            aqiDataToMapModel.setLatitude(device.getLatitude());
        }
        return aqiDataToMapModels;
    }

    @Override
    public List<AirAlarm> getAirAlarmHistory(AirQueryVo airQueryVo) {
        List<AirAlarm> airAlarms = new ArrayList<>(0);
        // 获取查询数据表
        List<String> tableNameList = TableNameUtil.tableNameList(TableNameUtil.Air_alarm, airQueryVo.getStartTime(), airQueryVo.getEndTime());
        // 时间格式换
        List<LocalDateTime> dateTimes = DateTimeUtil.queryTimeFormatter(airQueryVo.getStartTime(), airQueryVo.getEndTime());
        // 查询
        LambdaQueryWrapper<AirAlarm> query = Wrappers.<AirAlarm>lambdaQuery();
        for (String name : tableNameList) {
            MybatisPlusConfig.TableName.set(name);
            query.eq(AirAlarm::getDeviceNo, airQueryVo.getDeviceNo()).between(AirAlarm::getDateTime, dateTimes.get(0), dateTimes.get(1));
            List<AirAlarm> list = super.list(query);
            airAlarms.addAll(list);
        }
        return airAlarms;
    }
}
