package com.osen.aqms.modules.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.osen.aqms.common.config.MybatisPlusConfig;
import com.osen.aqms.common.model.AqiDataToMapModel;
import com.osen.aqms.common.model.AqiViewModel;
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
import java.time.format.DateTimeFormatter;
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
        if (addressVo.getAddress() == null || addressVo.getLevel() == null) {
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
            aqiDataToMapModel.setLive(device.getLive().equals(ConstUtil.OPEN_STATUS) ? "在线" : "离线");
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
            query.eq(AirAlarm::getDeviceNo, airQueryVo.getDeviceNo()).between(AirAlarm::getDateTime, dateTimes.get(0), dateTimes.get(1)).orderByAsc(AirAlarm::getDateTime);
            List<AirAlarm> list = super.list(query);
            airAlarms.addAll(list);
        }
        return airAlarms;
    }

    @Override
    public AqiViewModel getAlarmNumber(String deviceNo) {
        AqiViewModel aqiViewModel = new AqiViewModel();
        // 时间
        LocalDateTime end = LocalDateTime.now();
        LocalDateTime start = LocalDateTime.of(end.getYear(), end.getMonthValue(), 0, 0, 0, 0);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        // 请求体
        AirQueryVo airQueryVo = new AirQueryVo();
        airQueryVo.setDeviceNo(deviceNo);
        airQueryVo.setStartTime(start.format(formatter));
        airQueryVo.setEndTime(end.format(formatter));
        // 报警记录
        List<AirAlarm> airAlarmHistory = this.getAirAlarmHistory(airQueryVo);
        if (airAlarmHistory.size() <= 0)
            return aqiViewModel;
        // 报警次数
        int alarmNumber = airAlarmHistory.size();

        int pm25 = 0, pm10 = 0, so2 = 0, no2 = 0, co = 0, o3 = 0, tvoc = 0;
        String sensor = "";
        int maxData = 0;
        for (AirAlarm airAlarm : airAlarmHistory) {
            if (!airAlarm.getPm25Flag().equals("N")) {
                pm25 += pm25;
                if (pm25 > maxData) {
                    maxData = pm25;
                    sensor = "pm2.5";
                }
            }
            if (!airAlarm.getPm10Flag().equals("N")) {
                pm10 += pm10;
                if (pm10 > maxData) {
                    maxData = pm10;
                    sensor = "pm10";
                }
            }
            if (!airAlarm.getSo2Flag().equals("N")) {
                so2 += so2;
                if (so2 > maxData) {
                    maxData = so2;
                    sensor = "so2";
                }
            }
            if (!airAlarm.getNo2Flag().equals("N")) {
                no2 += no2;
                if (no2 > maxData) {
                    maxData = no2;
                    sensor = "no2";
                }
            }
            if (!airAlarm.getCoFlag().equals("N")) {
                co += co;
                if (co > maxData) {
                    maxData = co;
                    sensor = "co";
                }
            }
            if (!airAlarm.getO3Flag().equals("N")) {
                o3 += o3;
                if (pm25 > maxData) {
                    maxData = pm25;
                    sensor = "o3";
                }
            }
            if (!airAlarm.getVocFlag().equals("N")) {
                tvoc += tvoc;
                if (tvoc > maxData) {
                    maxData = tvoc;
                    sensor = "tvoc";
                }
            }
        }
        // top参数因子
        String topSensor = sensor;

        // 报警时间
        AirAlarm airAlarm = airAlarmHistory.get(alarmNumber - 1);
        LocalDateTime alarmDate = airAlarm.getDateTime();

        // 报警参数因子
        StringBuilder builder = new StringBuilder();
        if (!airAlarm.getPm25Flag().equals("N"))
            builder.append("pm2.5 ");
        if (!airAlarm.getPm10Flag().equals("N"))
            builder.append("pm10 ");
        if (!airAlarm.getSo2Flag().equals("N"))
            builder.append("so2 ");
        if (!airAlarm.getNo2Flag().equals("N"))
            builder.append("no2 ");
        if (!airAlarm.getCoFlag().equals("N"))
            builder.append("co ");
        if (!airAlarm.getO3Flag().equals("N"))
            builder.append("o3 ");
        if (!airAlarm.getVocFlag().equals("N"))
            builder.append("tvoc ");
        String alarmSensor = builder.toString();

        aqiViewModel.setAlarmNumber(alarmNumber);
        aqiViewModel.setAlarmDate(alarmDate);
        aqiViewModel.setAlarmSensor(alarmSensor);
        aqiViewModel.setTopSensor(topSensor);

        return aqiViewModel;
    }
}
