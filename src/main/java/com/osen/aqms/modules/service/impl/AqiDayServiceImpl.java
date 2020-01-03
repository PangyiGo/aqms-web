package com.osen.aqms.modules.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.osen.aqms.common.config.MybatisPlusConfig;
import com.osen.aqms.common.model.*;
import com.osen.aqms.common.requestVo.AirQueryVo;
import com.osen.aqms.common.requestVo.AqiCompareVo;
import com.osen.aqms.common.requestVo.AqiReportVo;
import com.osen.aqms.common.requestVo.PolluteMapVo;
import com.osen.aqms.common.utils.ConstUtil;
import com.osen.aqms.common.utils.DateTimeUtil;
import com.osen.aqms.common.utils.SecurityUtil;
import com.osen.aqms.common.utils.TableNameUtil;
import com.osen.aqms.modules.entity.data.AqiDay;
import com.osen.aqms.modules.entity.system.Device;
import com.osen.aqms.modules.mapper.data.AqiDayMapper;
import com.osen.aqms.modules.service.AqiDayService;
import com.osen.aqms.modules.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * User: PangYi
 * Date: 2019-11-30
 * Time: 10:52
 * Description:
 */
@Service
public class AqiDayServiceImpl extends ServiceImpl<AqiDayMapper, AqiDay> implements AqiDayService {

    @Autowired
    private DeviceService deviceService;

    @Override
    public List<AqiHistoryToDay> getAqiDayHistory(AirQueryVo airQueryVo) {
        List<AqiHistoryToDay> aqiHistoryToDays = new ArrayList<>(0);
        // 获取查询的数据表名称
        List<String> tableNameList = TableNameUtil.tableNameList(TableNameUtil.Aqi_day, airQueryVo.getStartTime(), airQueryVo.getEndTime());
        // 获取查询时间
        List<LocalDateTime> localDateTimes = DateTimeUtil.queryTimeFormatter(airQueryVo.getStartTime(), airQueryVo.getEndTime());
        // 查询
        List<AqiDay> aqiDays = new ArrayList<>(0);
        LambdaQueryWrapper<AqiDay> query = Wrappers.<AqiDay>lambdaQuery();
        for (String name : tableNameList) {
            MybatisPlusConfig.TableName.set(name);
            query.select(AqiDay::getDeviceNo, AqiDay::getDateTime, AqiDay::getAqi, AqiDay::getLevel, AqiDay::getQuality, AqiDay::getPollute, AqiDay::getPm25, AqiDay::getPm10, AqiDay::getSo2, AqiDay::getNo2, AqiDay::getCo, AqiDay::getO3, AqiDay::getVoc);
            query.eq(AqiDay::getDeviceNo, airQueryVo.getDeviceNo()).between(AqiDay::getDateTime, localDateTimes.get(0), localDateTimes.get(1));
            aqiDays.addAll(super.list(query));
        }
        for (AqiDay aqiDay : aqiDays) {
            AqiHistoryToDay aqiHistoryToDay = new AqiHistoryToDay();
            BeanUtil.copyProperties(aqiDay, aqiHistoryToDay);
            aqiHistoryToDays.add(aqiHistoryToDay);
        }
        return aqiHistoryToDays;
    }

    @Override
    public List<AqiReportToDayModel> getAqiReportToDay(AqiReportVo aqiReportVo) {
        List<AqiReportToDayModel> aqiReportToDayModels = new ArrayList<>(0);
        List<Device> deviceList = new ArrayList<>(0);
        if ((aqiReportVo.getAddress() == null && aqiReportVo.getLevel() == null) || ("".equals(aqiReportVo.getAddress().trim()) && "".equals(aqiReportVo.getLevel().trim()))) {
            // 查询全部设备列表
            deviceList = deviceService.findDeviceAllToUsername(SecurityUtil.getUsername());
        } else {
            // 按区域查询
            deviceList = deviceService.findDeviceGroupByAddress(aqiReportVo.getAddress(), aqiReportVo.getLevel());
        }
        // 数据表
        String tableName = TableNameUtil.generateTableName(TableNameUtil.Aqi_day, aqiReportVo.getTime(), ConstUtil.QUERY_DATE);
        // 时间
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(ConstUtil.QUERY_DATE);
        LocalDate localDate = LocalDate.parse(aqiReportVo.getTime(), formatter);
        LocalDateTime start = LocalDateTime.of(localDate.getYear(), localDate.getMonthValue(), localDate.getDayOfMonth(), 0, 0, 0);
        LocalDateTime end = LocalDateTime.of(localDate.getYear(), localDate.getMonthValue(), localDate.getDayOfMonth(), 23, 59, 59);
        for (Device device : deviceList) {
            AqiReportToDayModel hourModel = new AqiReportToDayModel();
            hourModel.setDeviceNo(device.getDeviceNo());
            hourModel.setDeviceName(device.getDeviceName());
            hourModel.setDateTime(start);
            // 查询
            LambdaQueryWrapper<AqiDay> query = Wrappers.<AqiDay>lambdaQuery();
            MybatisPlusConfig.TableName.set(tableName);
            query.eq(AqiDay::getDeviceNo, device.getDeviceNo()).between(AqiDay::getDateTime, start, end);
            AqiDay aqiDay = super.getOne(query);
            if (aqiDay == null) {
                // 添加
                aqiReportToDayModels.add(hourModel);
            } else {
                hourModel.setAqi(aqiDay.getAqi() + "");
                hourModel.setQuality(aqiDay.getQuality());
                hourModel.setLevel(aqiDay.getLevel() + "");
                hourModel.setPollute(aqiDay.getPollute());
                hourModel.setData(aqiDay.getData().toString());
                hourModel.setTips(aqiDay.getTips());
                hourModel.setPm25(aqiDay.getPm25().toString());
                hourModel.setPm10(aqiDay.getPm10().toString());
                hourModel.setSo2(aqiDay.getSo2().toString());
                hourModel.setNo2(aqiDay.getNo2().toString());
                hourModel.setCo(aqiDay.getCo().toString());
                hourModel.setO3(aqiDay.getO3().toString());
                hourModel.setVoc(aqiDay.getVoc().toString());
                hourModel.setPm25Index(aqiDay.getPm25Index() + "");
                hourModel.setPm10Index(aqiDay.getPm10Index() + "");
                hourModel.setSo2Index(aqiDay.getSo2Index() + "");
                hourModel.setNo2Index(aqiDay.getNo2Index() + "");
                hourModel.setCoIndex(aqiDay.getCoIndex() + "");
                hourModel.setO3Index(aqiDay.getO3Index() + "");
                // 添加
                aqiReportToDayModels.add(hourModel);
            }
        }
        return aqiReportToDayModels;
    }

    @Override
    public AqiCompareDataModel getAqiDayToCompare(AqiCompareVo aqiCompareVo) {
        // 设备号
        String deviceNo1 = aqiCompareVo.getDev1();
        String deviceNo2 = aqiCompareVo.getDev2();
        // 获取查询数据表
        List<String> nameList = TableNameUtil.tableNameList(TableNameUtil.Aqi_day, aqiCompareVo.getStartTime(), aqiCompareVo.getEndTime());
        // 查询时间
        List<LocalDateTime> localDateTimes = DateTimeUtil.queryTimeFormatter(aqiCompareVo.getStartTime(), aqiCompareVo.getEndTime());

        List<AqiDay> first = new ArrayList<>(0);
        List<AqiDay> second = new ArrayList<>(0);
        // 查询
        LambdaQueryWrapper<AqiDay> wrapper1 = Wrappers.<AqiDay>lambdaQuery().select(AqiDay::getDeviceNo, AqiDay::getAqi, AqiDay::getDateTime, AqiDay::getPm25, AqiDay::getPm10, AqiDay::getSo2, AqiDay::getNo2, AqiDay::getCo, AqiDay::getO3, AqiDay::getVoc);
        LambdaQueryWrapper<AqiDay> wrapper2 = Wrappers.<AqiDay>lambdaQuery().select(AqiDay::getDeviceNo, AqiDay::getAqi, AqiDay::getDateTime, AqiDay::getPm25, AqiDay::getPm10, AqiDay::getSo2, AqiDay::getNo2, AqiDay::getCo, AqiDay::getO3, AqiDay::getVoc);
        for (String name : nameList) {
            MybatisPlusConfig.TableName.set(name);
            wrapper1.eq(AqiDay::getDeviceNo, deviceNo1).between(AqiDay::getDateTime, localDateTimes.get(0), localDateTimes.get(1));
            List<AqiDay> list1 = super.list(wrapper1);
            if (list1 != null)
                first.addAll(list1);
            MybatisPlusConfig.TableName.set(name);
            wrapper2.eq(AqiDay::getDeviceNo, deviceNo2).between(AqiDay::getDateTime, localDateTimes.get(0), localDateTimes.get(1));
            List<AqiDay> list2 = super.list(wrapper2);
            if (list2 != null)
                second.addAll(list2);
        }
        List<AqiHistoryToHour> d1 = new ArrayList<>(0);
        List<AqiHistoryToHour> d2 = new ArrayList<>(0);
        for (AqiDay AqiDay : first) {
            AqiHistoryToHour hour = new AqiHistoryToHour();
            BeanUtil.copyProperties(AqiDay, hour);
            d1.add(hour);
        }
        for (AqiDay AqiDay : second) {
            AqiHistoryToHour hour = new AqiHistoryToHour();
            BeanUtil.copyProperties(AqiDay, hour);
            d2.add(hour);
        }
        AqiCompareDataModel aqiCompareDataModel = new AqiCompareDataModel();
        aqiCompareDataModel.setFirstDeviceData(d1);
        aqiCompareDataModel.setSecondDeviceData(d2);
        return aqiCompareDataModel;
    }

    @Override
    public LevelDayModel getLevelNumber(AirQueryVo airQueryVo) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(ConstUtil.QUERY_DATE);
        LocalDate localDate = LocalDate.now();
        LocalDate start = LocalDate.of(localDate.getYear(), localDate.getMonthValue(), 1);
        LocalDate end = LocalDate.of(localDate.getYear(), localDate.getMonthValue(), localDate.getMonth().maxLength());
        airQueryVo.setStartTime(start.format(formatter));
        airQueryVo.setEndTime(end.format(formatter));
        List<AqiHistoryToDay> aqiDayHistory = this.getAqiDayHistory(airQueryVo);
        LevelDayModel levelDayModel = new LevelDayModel();
        for (AqiHistoryToDay history : aqiDayHistory) {
            switch (history.getLevel()) {
                case 1:
                    levelDayModel.setLv1(levelDayModel.getLv1() + 1);
                    break;
                case 2:
                    levelDayModel.setLv2(levelDayModel.getLv2() + 1);
                    break;
                case 3:
                    levelDayModel.setLv3(levelDayModel.getLv3() + 1);
                    break;
                case 4:
                    levelDayModel.setLv4(levelDayModel.getLv4() + 1);
                    break;
                case 5:
                    levelDayModel.setLv5(levelDayModel.getLv5() + 1);
                    break;
                case 6:
                    levelDayModel.setLv6(levelDayModel.getLv6() + 1);
                    break;
            }
        }
        return levelDayModel;
    }

    @Override
    public List<PolluteMapResultModel> getSensorData(PolluteMapVo polluteMapVo, String sensor) {
        List<PolluteMapResultModel> mapResultModel = new ArrayList<>(0);
        // 当前用户设备列表
        String username = SecurityUtil.getUsername();
        List<Device> deviceList = deviceService.findDeviceAllToUsername(username);
        if (deviceList.size() <= 0)
            return mapResultModel;
        // 设备号列表
        List<String> deviceNos = new ArrayList<>(0);
        deviceList.forEach(device -> deviceNos.add(device.getDeviceNo()));
        // 时间格式化
        List<LocalDateTime> dateTimes = DateTimeUtil.queryTimeFormatter(polluteMapVo.getStartTime(), polluteMapVo.getEndTime());
        LocalDateTime startTime = dateTimes.get(0);
        LocalDateTime endTime = dateTimes.get(1);
        LocalDateTime initTime = LocalDateTime.of(2019, 12, 1, 0, 0, 0);
        if (startTime.isBefore(initTime))
            startTime = initTime;
        LocalDateTime tempTime = startTime;
        while (tempTime.isBefore(endTime)) {
            PolluteMapResultModel model = new PolluteMapResultModel();
            // 数据表
            String tableName = TableNameUtil.Aqi_day + "_" + tempTime.getYear() + ((tempTime.getMonthValue() < 10) ? "0" + tempTime.getMonthValue() : "" + tempTime.getMonthValue());
            // 查询数据
            List<SensorMapperModel> sensorModel = baseMapper.getSensorModel(tableName, deviceNos, sensor, tempTime);
            if (sensorModel == null || sensorModel.size() <= 0) {
                tempTime = tempTime.plusDays(1);
                continue;
            }
            // 最大值
            int maxValue = Objects.requireNonNull(sensorModel.stream().filter(Objects::nonNull).max(Comparator.comparing(SensorMapperModel::getNumber)).orElse(null)).getNumber();
            SensorTimeModel sensorTimeModel = new SensorTimeModel();
            List<SensorDataModel> sensorDataModels = new ArrayList<>(0);
            for (SensorMapperModel sensorMapperModel : sensorModel) {
                SensorDataModel sensorDataModel = new SensorDataModel();
                // 查询设备
                List<Device> list = deviceList.stream().filter(device -> device.getDeviceNo().equals(sensorMapperModel.getDeviceNo())).collect(Collectors.toList());
                if (list.size() != 1)
                    continue;
                sensorDataModel.setLng(list.get(0).getLongitude());
                sensorDataModel.setLat(list.get(0).getLatitude());
                sensorDataModel.setCount(sensorMapperModel.getNumber());
                sensorDataModels.add(sensorDataModel);
            }
            sensorTimeModel.setMax(maxValue);
            sensorTimeModel.setSensorDataModels(sensorDataModels);

            // 时间 数据
            model.setDateTime(tempTime);
            model.setSensorTimeModel(sensorTimeModel);

            mapResultModel.add(model);

            tempTime = tempTime.plusDays(1);
        }
        return mapResultModel;
    }

    @Override
    public List<AqiSensorDayModel> getAqiSensorHistory(String deviceNo, String type, String sensor) {
        List<AqiSensorDayModel> aqiSensorDayModels = new ArrayList<>(0);
        // 时间格式化
        LocalDateTime dateTime = LocalDateTime.now();
        LocalDateTime startTime = null;
        LocalDateTime endTime;
        // 判断时间类型
        endTime = LocalDateTime.of(dateTime.getYear(), dateTime.getMonthValue(), dateTime.getDayOfMonth(), 0, 0, 0);
        if (Integer.parseInt(type) == 2) {
            startTime = endTime.minusWeeks(1);
        }
        if (Integer.parseInt(type) == 3) {
            startTime = endTime.minusMonths(1);
        }
        // 生成数据表
        assert startTime != null;
        List<String> tableNameList = TableNameUtil.tableNameList(TableNameUtil.Aqi_day, startTime, endTime);
        for (String tableName : tableNameList) {
            List<AqiSensorDayModel> sensorHistory = baseMapper.getSensorHistory(tableName, deviceNo, startTime, endTime, sensor);
            aqiSensorDayModels.addAll(sensorHistory);
        }

        int number = 0;
        if (Integer.parseInt(type) + 1 == 2) {
            number = 7;
        } else {
            number = 30;
        }
        List<AqiSensorDayModel> res = new ArrayList<>(0);
        LocalDateTime temp = startTime.plusDays(1);
        for (int i = 0; i < number; i++) {
            AqiSensorDayModel aqiDataModel = new AqiSensorDayModel();
            LocalDateTime finalTemp = temp;
            List<AqiSensorDayModel> modelList = aqiSensorDayModels.stream().filter(aqiSensorModel -> aqiSensorModel.getDateTime().isEqual(finalTemp)).collect(Collectors.toList());
            if (modelList.size() == 1) {
                AqiSensorDayModel model = modelList.get(0);
                aqiDataModel.setDateTime(model.getDateTime());
                aqiDataModel.setNumber(model.getNumber());
            } else {
                aqiDataModel.setDateTime(temp);
                aqiDataModel.setNumber(new BigDecimal(0));
            }
            res.add(aqiDataModel);
            // 时间修改
            temp = temp.plusDays(1);
        }

        return res;
    }
}
