package com.osen.aqms.modules.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.osen.aqms.common.config.MybatisPlusConfig;
import com.osen.aqms.common.enums.AirSensor;
import com.osen.aqms.common.exception.type.ControllerException;
import com.osen.aqms.common.model.*;
import com.osen.aqms.common.requestVo.*;
import com.osen.aqms.common.utils.*;
import com.osen.aqms.modules.entity.data.AirHistory;
import com.osen.aqms.modules.entity.data.AqiDay;
import com.osen.aqms.modules.entity.system.Device;
import com.osen.aqms.modules.mapper.data.AirHistoryMapper;
import com.osen.aqms.modules.service.AirHistoryService;
import com.osen.aqms.modules.service.DeviceService;
import com.osen.aqms.web.data_air.utils.AirSensorHistoryExport;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * User: PangYi
 * Date: 2019-11-30
 * Time: 10:52
 * Description:
 */
@Service
public class AirHistoryServiceImpl extends ServiceImpl<AirHistoryMapper, AirHistory> implements AirHistoryService {

    @Autowired
    private RedisOpsUtil redisOpsUtil;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private AirSensorHistoryExport airSensorHistoryExport;

    @Override
    public List<AirDataModel> getAirDataToSensor(String deviceNO, String sensor) {
        List<AirDataModel> airDataModels = new ArrayList<>(0);
        // 获取设备信息
        Device device = deviceService.findOneDeviceToNo(deviceNO);
        if (device == null)
            throw new ControllerException("无法查询设备");
        // 查询时间
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = startTime.minusHours(12);
        // 基本数据表
        List<String> nameList = TableNameUtil.tableNameList(TableNameUtil.Air_history, endTime, startTime);
        if (nameList.size() <= 0)
            return airDataModels;
        // 参数因子
        LambdaQueryWrapper<AirHistory> query = Wrappers.<AirHistory>lambdaQuery();
        if (AirSensor.PM25.getName().equals(sensor)) {
            for (String name : nameList) {
                MybatisPlusConfig.TableName.set(name);
                query.select(AirHistory::getDateTime, AirHistory::getPm25).eq(AirHistory::getDeviceNo, deviceNO).between(AirHistory::getDateTime, endTime, startTime);
                List<AirHistory> histories = super.list(query);
                if (histories != null) {
                    for (AirHistory history : histories) {
                        AirDataModel airDataModel = new AirDataModel();
                        airDataModel.setData(history.getPm25());
                        airDataModel.setDateTime(history.getDateTime());
                        airDataModels.add(airDataModel);
                    }
                }
            }
        }
        if (AirSensor.PM10.getName().equals(sensor)) {
            for (String name : nameList) {
                MybatisPlusConfig.TableName.set(name);
                query.select(AirHistory::getDateTime, AirHistory::getPm10).eq(AirHistory::getDeviceNo, deviceNO).between(AirHistory::getDateTime, endTime, startTime);
                List<AirHistory> histories = super.list(query);
                if (histories != null) {
                    for (AirHistory history : histories) {
                        AirDataModel airDataModel = new AirDataModel();
                        airDataModel.setData(history.getPm10());
                        airDataModel.setDateTime(history.getDateTime());
                        airDataModels.add(airDataModel);
                    }
                }
            }
        }
        if (AirSensor.SO2.getName().equals(sensor)) {
            for (String name : nameList) {
                MybatisPlusConfig.TableName.set(name);
                query.select(AirHistory::getDateTime, AirHistory::getSo2).eq(AirHistory::getDeviceNo, deviceNO).between(AirHistory::getDateTime, endTime, startTime);
                List<AirHistory> histories = super.list(query);
                if (histories != null) {
                    for (AirHistory history : histories) {
                        AirDataModel airDataModel = new AirDataModel();
                        airDataModel.setData(history.getSo2());
                        airDataModel.setDateTime(history.getDateTime());
                        airDataModels.add(airDataModel);
                    }
                }
            }
        }
        if (AirSensor.NO2.getName().equals(sensor)) {
            for (String name : nameList) {
                MybatisPlusConfig.TableName.set(name);
                query.select(AirHistory::getDateTime, AirHistory::getNo2).eq(AirHistory::getDeviceNo, deviceNO).between(AirHistory::getDateTime, endTime, startTime);
                List<AirHistory> histories = super.list(query);
                if (histories != null) {
                    for (AirHistory history : histories) {
                        AirDataModel airDataModel = new AirDataModel();
                        airDataModel.setData(history.getNo2());
                        airDataModel.setDateTime(history.getDateTime());
                        airDataModels.add(airDataModel);
                    }
                }
            }
        }
        if (AirSensor.CO.getName().equals(sensor)) {
            for (String name : nameList) {
                MybatisPlusConfig.TableName.set(name);
                query.select(AirHistory::getDateTime, AirHistory::getCo).eq(AirHistory::getDeviceNo, deviceNO).between(AirHistory::getDateTime, endTime, startTime);
                List<AirHistory> histories = super.list(query);
                if (histories != null) {
                    for (AirHistory history : histories) {
                        AirDataModel airDataModel = new AirDataModel();
                        airDataModel.setData(history.getCo());
                        airDataModel.setDateTime(history.getDateTime());
                        airDataModels.add(airDataModel);
                    }
                }
            }
        }
        if (AirSensor.O3.getName().equals(sensor)) {
            for (String name : nameList) {
                MybatisPlusConfig.TableName.set(name);
                query.select(AirHistory::getDateTime, AirHistory::getO3).eq(AirHistory::getDeviceNo, deviceNO).between(AirHistory::getDateTime, endTime, startTime);
                List<AirHistory> histories = super.list(query);
                if (histories != null) {
                    for (AirHistory history : histories) {
                        AirDataModel airDataModel = new AirDataModel();
                        airDataModel.setData(history.getO3());
                        airDataModel.setDateTime(history.getDateTime());
                        airDataModels.add(airDataModel);
                    }
                }
            }
        }
        if (AirSensor.VOC.getName().equals(sensor)) {
            for (String name : nameList) {
                MybatisPlusConfig.TableName.set(name);
                query.select(AirHistory::getDateTime, AirHistory::getVoc).eq(AirHistory::getDeviceNo, deviceNO).between(AirHistory::getDateTime, endTime, startTime);
                List<AirHistory> histories = super.list(query);
                if (histories != null) {
                    for (AirHistory history : histories) {
                        AirDataModel airDataModel = new AirDataModel();
                        airDataModel.setData(history.getVoc());
                        airDataModel.setDateTime(history.getDateTime());
                        airDataModels.add(airDataModel);
                    }
                }
            }
        }
        return airDataModels;
    }

    @Override
    public AirRealTimeModel getAirRealtime(String deviceNo) {
        AirRealTimeModel realTimeModel = new AirRealTimeModel();
        // 获取设备信息
        Device device = deviceService.findOneDeviceToNo(deviceNo);
        if (device == null)
            throw new ControllerException("无法查询设备");
        realTimeModel.setDevice(device);

        // 获取实时数据
        AqiRealtimeModel aqiRealtimeModel = new AqiRealtimeModel();
        String dataJson = redisOpsUtil.getToMap(TableNameUtil.Air_Realtime, deviceNo);
        if (!StrUtil.isEmpty(dataJson))
            aqiRealtimeModel = JSON.parseObject(dataJson, AqiRealtimeModel.class);
        realTimeModel.setAqiRealtimeModel(aqiRealtimeModel);

        // 默认获取近12小时的PM2.5参数历史曲线
        List<AirDataModel> airDataToSensor = this.getAirDataToSensor(deviceNo, AirSensor.PM25.getName());
        realTimeModel.setDataModels(airDataToSensor);

        return realTimeModel;
    }

    @Override
    public List<AqiDataToMapModel> getAirRealtimeList(Map<String, Object> params) {
        List<Device> deviceList;
        if (params == null) {
            // 查询全部设备列表数据
            String username = SecurityUtil.getUsername();
            deviceList = deviceService.findDeviceAllToUsername(username);
        } else {
            // 查询
            String address = (String) params.get("address");
            String level = (String) params.get("level");
            deviceList = deviceService.findDeviceGroupByAddress(address, level);
        }
        // 数据获取
        return this.aqiDataWrapper(deviceList);
    }

    @Override
    public List<AqiDataToMapModel> getAirRankToRealtime(AirRankVo airRankVo) {
        List<Device> deviceList;
        if (airRankVo == null) {
            // 默认查询全部
            String username = SecurityUtil.getUsername();
            deviceList = deviceService.findDeviceAllToUsername(username);
        } else {
            deviceList = deviceService.findDeviceGroupByAddress(airRankVo.getAddress(), airRankVo.getLevel());
        }
        // 数据获取
        List<AqiDataToMapModel> mapModels = this.aqiDataWrapper(deviceList);
        // AQI升序排名
        mapModels = mapModels.stream().sorted(Comparator.comparing(AqiDataToMapModel::getAqi)).collect(Collectors.toList());
        return mapModels;
    }

    /**
     * 数据封装
     *
     * @param deviceList 设备列表
     */
    private List<AqiDataToMapModel> aqiDataWrapper(List<Device> deviceList) {
        List<AqiDataToMapModel> aqiDataToMapModels = new ArrayList<>(0);
        for (Device device : deviceList) {
            AqiDataToMapModel aqiDataModel = airSensorHistoryExport.getAqiDataModel(device);
            aqiDataToMapModels.add(aqiDataModel);
        }
        // 除去aqi为0的数据
        return aqiDataToMapModels.stream().filter(aqiDataToMapModel -> aqiDataToMapModel.getAqi() != 0 && aqiDataToMapModel.getLevel() != 0).collect(Collectors.toList());
    }

    @Override
    public List<AqiDataToMapModel> getAirRankToHistory(AirRankVo airRankVo) {
        List<AqiDataToMapModel> aqiDataToMapModels = new ArrayList<>(0);
        List<Device> deviceList;
        if ((airRankVo.getAddress() == null && airRankVo.getLevel() == null) || ("".equals(airRankVo.getAddress().trim()) && "".equals(airRankVo.getLevel().trim()))) {
            // 获取全部设备
            deviceList = deviceService.findDeviceAllToUsername(SecurityUtil.getUsername());
        } else {
            deviceList = deviceService.findDeviceGroupByAddress(airRankVo.getAddress(), airRankVo.getLevel());
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(ConstUtil.QUERY_DATE);
        LocalDate time = LocalDate.parse(airRankVo.getTime(), formatter);
        // TableName
        int year = time.getYear();
        String month = (time.getMonthValue() < 10) ? "0" + time.getMonthValue() : "" + time.getMonthValue();
        String tableName = TableNameUtil.Air_history + "_" + year + month;
        for (Device device : deviceList) {
            AqiDataToMapModel aqiDataModel = airSensorHistoryExport.getAqiDataModel(device);
            // 数据
            LocalDateTime startTime = LocalDateTime.of(time.getYear(), time.getMonthValue(), time.getDayOfMonth(), 0, 0, 0);
            LocalDateTime endTime = LocalDateTime.of(time.getYear(), time.getMonthValue(), time.getDayOfMonth(), 23, 59, 59);
            AirAvgModel avgToDay = baseMapper.getAvgToDay(tableName, device.getDeviceNo(), startTime, endTime);
            if (avgToDay != null) {
                AqiDay aqiDay = AQIComputedUtil.computedAqiToDay(device.getDeviceNo(), startTime, avgToDay);
                aqiDataModel.setPm25(aqiDay.getPm25().setScale(1, BigDecimal.ROUND_DOWN));
                aqiDataModel.setPm10(aqiDay.getNo2().setScale(1, BigDecimal.ROUND_DOWN));
                aqiDataModel.setSo2(aqiDay.getSo2().setScale(1, BigDecimal.ROUND_DOWN));
                aqiDataModel.setNo2(aqiDay.getNo2().setScale(1, BigDecimal.ROUND_DOWN));
                aqiDataModel.setCo(aqiDay.getCo().setScale(1, BigDecimal.ROUND_DOWN));
                aqiDataModel.setO3(aqiDay.getO3().setScale(1, BigDecimal.ROUND_DOWN));
                aqiDataModel.setVoc(aqiDay.getVoc().setScale(1, BigDecimal.ROUND_DOWN));
                aqiDataModel.setAqi(aqiDay.getAqi());
                aqiDataModel.setPollute(aqiDay.getPollute());
                aqiDataModel.setLevel(aqiDay.getLevel());
                aqiDataModel.setQuality(aqiDay.getQuality());
                aqiDataModel.setDateTime(aqiDay.getDateTime());
                aqiDataToMapModels.add(aqiDataModel);
            }
        }
        // AQI升序排名
        aqiDataToMapModels = aqiDataToMapModels.stream().filter(aqiDataToMapModel -> aqiDataToMapModel.getAqi() != 0 && aqiDataToMapModel.getLevel() != 0).sorted(Comparator.comparing(AqiDataToMapModel::getAqi)).collect(Collectors.toList());
        return aqiDataToMapModels;
    }

    @Override
    public List<AirQueryDataModel> getAirRealtimeHistory(AirQueryVo airQueryVo) {
        List<AirQueryDataModel> airQueryDataModels = new ArrayList<>(0);
        // 获取查询数据表
        List<String> tableNameList = TableNameUtil.tableNameList(TableNameUtil.Air_history, airQueryVo.getStartTime(), airQueryVo.getEndTime());
        // 时间格式换
        List<LocalDateTime> dateTimes = DateTimeUtil.queryTimeFormatter(airQueryVo.getStartTime(), airQueryVo.getEndTime());
        // 查询
        List<AirHistory> historyList = new ArrayList<>(0);
        LambdaQueryWrapper<AirHistory> query = Wrappers.<AirHistory>lambdaQuery();
        for (String name : tableNameList) {
            MybatisPlusConfig.TableName.set(name);
            query.select(AirHistory::getDeviceNo, AirHistory::getPm25, AirHistory::getPm10, AirHistory::getSo2, AirHistory::getNo2, AirHistory::getCo, AirHistory::getO3, AirHistory::getVoc, AirHistory::getDateTime);
            query.eq(AirHistory::getDeviceNo, airQueryVo.getDeviceNo()).between(AirHistory::getDateTime, dateTimes.get(0), dateTimes.get(1));
            // 添加
            historyList.addAll(super.list(query));
        }
        for (AirHistory airHistory : historyList) {
            AirQueryDataModel airQueryDataModel = new AirQueryDataModel();
            BeanUtil.copyProperties(airHistory, airQueryDataModel);
            airQueryDataModels.add(airQueryDataModel);
        }
        return airQueryDataModels;
    }

    @Override
    public List<AirAccordModel> getAirAccordToDay(AirAccordVo airAccordVo) {
        return this.warpperModel(1, airAccordVo);
    }

    @Override
    public List<AirAccordModel> getAirAccordToMonth(AirAccordVo airAccordVo) {
        return this.warpperModel(2, airAccordVo);
    }

    /**
     * @param type 1表示day报表，2表示month报表
     * @return 信息
     */
    private List<AirAccordModel> warpperModel(int type, AirAccordVo airAccordVo) {
        List<AirAccordModel> airAccordModels = new ArrayList<>(0);
        List<Device> deviceList;
        if ((airAccordVo.getAddress() == null && airAccordVo.getLevel() == null) || ("".equals(airAccordVo.getAddress().trim()) && "".equals(airAccordVo.getLevel().trim()))) {
            // 获取全部设备
            deviceList = deviceService.findDeviceAllToUsername(SecurityUtil.getUsername());
        } else {
            deviceList = deviceService.findDeviceGroupByAddress(airAccordVo.getAddress(), airAccordVo.getLevel());
        }
        LocalDateTime startTime = null;
        LocalDateTime endTime = null;
        if (type == 1) {
            // 计算日时间，格式化
            List<LocalDateTime> localDateTimes = DateTimeUtil.queryTimeFormatter(airAccordVo.getTime(), airAccordVo.getTime());
            startTime = localDateTimes.get(0);
            endTime = localDateTimes.get(1);
        }
        if (type == 2) {
            // 时间
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(ConstUtil.QUERY_DATE);
            LocalDate localDate = LocalDate.parse(airAccordVo.getTime(), formatter);
            startTime = LocalDateTime.of(localDate.getYear(), localDate.getMonthValue(), 1, 0, 0, 0);
            endTime = LocalDateTime.of(localDate.getYear(), localDate.getMonthValue(), localDate.getMonth().maxLength(), 23, 59, 59);
        }
        for (Device device : deviceList) {
            AirAccordModel accordModel = this.wrapperAirAccordModel(device, airAccordVo.getTime(), startTime, endTime);
            airAccordModels.add(accordModel);
        }
        airAccordModels = airAccordModels.stream().sorted(Comparator.comparing(AirAccordModel::getCount).reversed()).collect(Collectors.toList());
        return airAccordModels;
    }

    private AirAccordModel wrapperAirAccordModel(Device device, String time, LocalDateTime startTime, LocalDateTime endTime) {
        // 表名
        String tableName = TableNameUtil.generateTableName(TableNameUtil.Air_history, time, ConstUtil.QUERY_DATE);
        // 数据
        AirAccordModel accordModel = new AirAccordModel();
        accordModel.setDeviceNo(device.getDeviceNo());
        accordModel.setDeviceName(device.getDeviceName());
        AirAccordMapperModel airAccord = baseMapper.getAirAccord(tableName, device.getDeviceNo(), startTime, endTime);
        if (airAccord != null && airAccord.getPm25Avg() != null && airAccord.getPm10Avg() != null) {
            AirAvgModel airAvgModel = new AirAvgModel();
            BeanUtil.copyProperties(airAccord, airAvgModel);
            // 计算AQI
            AqiDay aqiDay = AQIComputedUtil.computedAqiToDay(null, null, airAvgModel);
            accordModel.setAqi(aqiDay.getAqi() + "");
            accordModel.setLevel(aqiDay.getLevel() + "");
            accordModel.setPollute(aqiDay.getPollute());
            // 统计数据
            accordModel.setCount(airAccord.getCount());
            accordModel.setPm25Avg(airAccord.getPm25Avg().setScale(1, BigDecimal.ROUND_DOWN));
            accordModel.setPm25Max(airAccord.getPm25Max().setScale(1, BigDecimal.ROUND_DOWN));
            accordModel.setPm25Min(airAccord.getPm25Min().setScale(1, BigDecimal.ROUND_DOWN));
            accordModel.setPm10Avg(airAccord.getPm10Avg().setScale(1, BigDecimal.ROUND_DOWN));
            accordModel.setPm10Max(airAccord.getPm10Max().setScale(1, BigDecimal.ROUND_DOWN));
            accordModel.setPm10Min(airAccord.getPm10Min().setScale(1, BigDecimal.ROUND_DOWN));
            accordModel.setSo2Avg(airAccord.getSo2Avg().setScale(1, BigDecimal.ROUND_DOWN));
            accordModel.setSo2Max(airAccord.getSo2Max().setScale(1, BigDecimal.ROUND_DOWN));
            accordModel.setSo2Min(airAccord.getSo2Min().setScale(1, BigDecimal.ROUND_DOWN));
            accordModel.setNo2Avg(airAccord.getNo2Avg().setScale(1, BigDecimal.ROUND_DOWN));
            accordModel.setNo2Max(airAccord.getNo2Max().setScale(1, BigDecimal.ROUND_DOWN));
            accordModel.setNo2Min(airAccord.getNo2Min().setScale(1, BigDecimal.ROUND_DOWN));
            accordModel.setCoAvg(airAccord.getCoAvg().setScale(1, BigDecimal.ROUND_DOWN));
            accordModel.setCoMax(airAccord.getCoMax().setScale(1, BigDecimal.ROUND_DOWN));
            accordModel.setCoMin(airAccord.getCoMin().setScale(1, BigDecimal.ROUND_DOWN));
            accordModel.setO3Avg(airAccord.getO3Avg().setScale(1, BigDecimal.ROUND_DOWN));
            accordModel.setO3Max(airAccord.getO3Max().setScale(1, BigDecimal.ROUND_DOWN));
            accordModel.setO3Min(airAccord.getO3Min().setScale(1, BigDecimal.ROUND_DOWN));
            accordModel.setVocAvg(airAccord.getVocAvg().setScale(1, BigDecimal.ROUND_DOWN));
            accordModel.setVocMax(airAccord.getVocMax().setScale(1, BigDecimal.ROUND_DOWN));
            accordModel.setVocMin(airAccord.getVocMin().setScale(1, BigDecimal.ROUND_DOWN));
        }
        return accordModel;
    }

    @Override
    public AirMonitorModel getAirMonitor(AirMonitorVo airMonitorVo) {
        // 数据封装体
        AirMonitorModel airMonitorModel = new AirMonitorModel();
        List<Device> deviceList;
        if ((airMonitorVo.getAddress() == null && airMonitorVo.getLevel() == null) || ("".equals(airMonitorVo.getAddress().trim()) && "".equals(airMonitorVo.getLevel().trim()))) {
            // 获取全部设备
            deviceList = deviceService.findDeviceAllToUsername(SecurityUtil.getUsername());
        } else {
            deviceList = deviceService.findDeviceGroupByAddress(airMonitorVo.getAddress(), airMonitorVo.getLevel());
        }
        for (Device device : deviceList) {
            Map<String, Object> map = new HashMap<>(0);
            List<LocalDateTime> localDateTimes = DateTimeUtil.queryTimeFormatter(airMonitorVo.getTime(), airMonitorVo.getTime());
            LocalDateTime startTime = localDateTimes.get(0);
            LocalDateTime endTime = localDateTimes.get(1);
            // 平均值，最大值，最小值
            AirAccordModel accordModel = this.wrapperAirAccordModel(device, airMonitorVo.getTime(), startTime, endTime);
            if (accordModel.getAqi().equals("Null")) {
                map.put("deviceNo", device.getDeviceNo());
                map.put("aqi", 0);
            } else {
                // aqi等级分布
                switch (accordModel.getLevel()) {
                    case "1":
                        airMonitorModel.setLv1(airMonitorModel.getLv1() + 1);
                        break;
                    case "2":
                        airMonitorModel.setLv2(airMonitorModel.getLv2() + 1);
                        break;
                    case "3":
                        airMonitorModel.setLv3(airMonitorModel.getLv3() + 1);
                        break;
                    case "4":
                        airMonitorModel.setLv4(airMonitorModel.getLv4() + 1);
                        break;
                    case "5":
                        airMonitorModel.setLv5(airMonitorModel.getLv5() + 1);
                        break;
                    case "6":
                        airMonitorModel.setLv6(airMonitorModel.getLv6() + 1);
                        break;
                }
                // 污染浓度分布
                switch (accordModel.getPollute()) {
                    case "PM2.5":
                        airMonitorModel.setPm25(airMonitorModel.getPm25() + 1);
                        break;
                    case "PM10":
                        airMonitorModel.setPm10(airMonitorModel.getPm10() + 1);
                        break;
                    case "SO2":
                        airMonitorModel.setSo2(airMonitorModel.getSo2() + 1);
                        break;
                    case "NO2":
                        airMonitorModel.setNo2(airMonitorModel.getNo2() + 1);
                        break;
                    case "CO":
                        airMonitorModel.setCo(airMonitorModel.getCo() + 1);
                        break;
                    case "O3":
                        airMonitorModel.setO3(airMonitorModel.getO3() + 1);
                        break;
                }
                map.put("deviceNo", device.getDeviceNo());
                map.put("aqi", Integer.valueOf(accordModel.getAqi()));
            }
            airMonitorModel.getAqiList().add(map);
            airMonitorModel.getAirAccordModels().add(accordModel);
        }
        return airMonitorModel;
    }

    @Override
    public HSSFWorkbook getAirSensorToExport(AirExportVo airExportVo) {
        // 设备号列表
        List<String> deviceNos = airExportVo.getDeviceNos();
        // 查询历史数据
        Map<String, List<AirQueryDataModel>> listMap = new HashMap<>(0);
        for (String deviceNo : deviceNos) {
            AirQueryVo airQueryVo = new AirQueryVo();
            airQueryVo.setDeviceNo(deviceNo);
            airQueryVo.setStartTime(airExportVo.getStartTime());
            airQueryVo.setEndTime(airExportVo.getEndTime());
            List<AirQueryDataModel> airRealtimeHistory = this.getAirRealtimeHistory(airQueryVo);
            // 设备信息
            Device device = deviceService.findOneDeviceToNo(deviceNo);
            String name = device.getDeviceNo() + "-" + device.getDeviceName();
            listMap.put(name, airRealtimeHistory);
        }
        return airSensorHistoryExport.exportToAirHistory(listMap);
    }

    @Override
    public AirSensorMaxModel getAirSensorMax(AddressVo addressVo) {
        AirSensorMaxModel airSensorMaxModel = new AirSensorMaxModel();
        List<Device> deviceList = deviceService.findDeviceGroupByAddress(addressVo.getAddress(), addressVo.getLevel());
        if (deviceList.size() <= 0)
            return airSensorMaxModel;
        // 数据最大值
        String pm25 = null;
        BigDecimal pm25Data = new BigDecimal(0);
        String pm10 = null;
        BigDecimal pm10Data = new BigDecimal(0);
        String so2 = null;
        BigDecimal so2Data = new BigDecimal(0);
        String no2 = null;
        BigDecimal no2Data = new BigDecimal(0);
        String o3 = null;
        BigDecimal o3Data = new BigDecimal(0);
        String co = null;
        BigDecimal coData = new BigDecimal(0);
        String voc = null;
        BigDecimal vocData = new BigDecimal(0);
        for (Device device : deviceList) {
            // 获取实时数据
            String dataJson = redisOpsUtil.getToMap(TableNameUtil.Air_Realtime, device.getDeviceNo());
            if (dataJson == null)
                continue;
            AqiRealtimeModel aqiRealtimeModel = JSON.parseObject(dataJson, AqiRealtimeModel.class);
            if (aqiRealtimeModel.getPm25().compareTo(pm25Data) > 0) {
                pm25 = device.getDeviceName();
                pm25Data = aqiRealtimeModel.getPm25();
            }
            if (aqiRealtimeModel.getPm10().compareTo(pm10Data) > 0) {
                pm10 = device.getDeviceName();
                pm10Data = aqiRealtimeModel.getPm10();
            }
            if (aqiRealtimeModel.getSo2().compareTo(so2Data) > 0) {
                so2 = device.getDeviceName();
                so2Data = aqiRealtimeModel.getSo2();
            }
            if (aqiRealtimeModel.getNo2().compareTo(no2Data) > 0) {
                no2 = device.getDeviceName();
                no2Data = aqiRealtimeModel.getNo2();
            }
            if (aqiRealtimeModel.getCo().compareTo(coData) > 0) {
                co = device.getDeviceName();
                coData = aqiRealtimeModel.getCo();
            }
            if (aqiRealtimeModel.getO3().compareTo(o3Data) > 0) {
                o3 = device.getDeviceName();
                o3Data = aqiRealtimeModel.getO3();
            }
            if (aqiRealtimeModel.getVoc().compareTo(vocData) > 0) {
                voc = device.getDeviceName();
                vocData = aqiRealtimeModel.getVoc();
            }
        }
        airSensorMaxModel.setPm25(pm25);
        airSensorMaxModel.setPm25Data(pm25Data);
        airSensorMaxModel.setPm10(pm10);
        airSensorMaxModel.setPm10Data(pm10Data);
        airSensorMaxModel.setSo2(so2);
        airSensorMaxModel.setSo2Data(so2Data);
        airSensorMaxModel.setNo2(no2);
        airSensorMaxModel.setNo2Data(no2Data);
        airSensorMaxModel.setCo(co);
        airSensorMaxModel.setCoData(coData);
        airSensorMaxModel.setO3(o3);
        airSensorMaxModel.setO3Data(o3Data);
        airSensorMaxModel.setVoc(voc);
        airSensorMaxModel.setVocData(vocData);
        return airSensorMaxModel;
    }
}
