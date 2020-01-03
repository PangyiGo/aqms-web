package com.osen.aqms.modules.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.osen.aqms.common.config.MybatisPlusConfig;
import com.osen.aqms.common.exception.type.ServiceException;
import com.osen.aqms.common.model.*;
import com.osen.aqms.common.requestVo.*;
import com.osen.aqms.common.utils.*;
import com.osen.aqms.modules.entity.data.AqiDay;
import com.osen.aqms.modules.entity.data.AqiHour;
import com.osen.aqms.modules.entity.system.Device;
import com.osen.aqms.modules.mapper.data.AqiHourMapper;
import com.osen.aqms.modules.service.AqiHourService;
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
public class AqiHourServiceImpl extends ServiceImpl<AqiHourMapper, AqiHour> implements AqiHourService {

    @Autowired
    private DeviceService deviceService;

    @Override
    public List<AqiDataModel> getAqi24HToDeviceNo(String deviceNo) {
        List<AqiHour> aqiHours = new ArrayList<>(0);
        // 时间
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime endTime = LocalDateTime.of(now.getYear(), now.getMonthValue(), now.getDayOfMonth(), now.getHour(), 0, 0);
        LocalDateTime startTime = endTime.minusHours(23);
        // 获取表名
        List<String> nameList = TableNameUtil.tableNameList(TableNameUtil.Aqi_hour, startTime, endTime);
        // 查询
        LambdaQueryWrapper<AqiHour> query = Wrappers.<AqiHour>lambdaQuery();
        for (String name : nameList) {
            MybatisPlusConfig.TableName.set(name);
            query.select(AqiHour::getDateTime, AqiHour::getAqi).eq(AqiHour::getDeviceNo, deviceNo).between(AqiHour::getDateTime, startTime, endTime).orderByAsc(AqiHour::getDateTime);
            List<AqiHour> hourList = super.list(query);
            if (hourList != null)
                aqiHours.addAll(hourList);
        }
        // 数据封装
        List<AqiDataModel> aqiDataModels = new ArrayList<>(0);

        LocalDateTime temp = startTime;
        for (int i = 0; i < 24; i++) {
            AqiDataModel aqiDataModel = new AqiDataModel();
            LocalDateTime finalTemp = temp;
            List<AqiHour> hours = aqiHours.stream().filter(aqiHour -> aqiHour.getDateTime().isEqual(finalTemp)).collect(Collectors.toList());
            if (hours.size() == 1) {
                AqiHour hour = hours.get(0);
                aqiDataModel.setDateTime(hour.getDateTime());
                aqiDataModel.setData(hour.getAqi());
            } else {
                aqiDataModel.setDateTime(temp);
                aqiDataModel.setData(0);
            }
            aqiDataModels.add(aqiDataModel);
            // 时间修改
            temp = temp.plusHours(1);
        }
        return aqiDataModels;
    }

    @Override
    public List<AqiHistoryToHour> getAqiHourHistory(AirQueryVo airQueryVo) {
        List<AqiHistoryToHour> aqiHistoryToHours = new ArrayList<>(0);
        // 获取查询的数据表名称
        List<String> tableNameList = TableNameUtil.tableNameList(TableNameUtil.Aqi_hour, airQueryVo.getStartTime(), airQueryVo.getEndTime());
        // 获取查询时间
        List<LocalDateTime> localDateTimes = DateTimeUtil.queryTimeFormatter(airQueryVo.getStartTime(), airQueryVo.getEndTime());
        // 查询
        List<AqiHour> aqiHours = new ArrayList<>(0);
        LambdaQueryWrapper<AqiHour> query = Wrappers.<AqiHour>lambdaQuery();
        for (String name : tableNameList) {
            MybatisPlusConfig.TableName.set(name);
            query.select(AqiHour::getDeviceNo, AqiHour::getDateTime, AqiHour::getAqi, AqiHour::getLevel, AqiHour::getQuality, AqiHour::getPollute, AqiHour::getPm25, AqiHour::getPm10, AqiHour::getSo2, AqiHour::getNo2, AqiHour::getCo, AqiHour::getO3, AqiHour::getVoc);
            query.eq(AqiHour::getDeviceNo, airQueryVo.getDeviceNo()).between(AqiHour::getDateTime, localDateTimes.get(0), localDateTimes.get(1));
            aqiHours.addAll(super.list(query));
        }
        for (AqiHour aqiHour : aqiHours) {
            AqiHistoryToHour aqiHistoryToHour = new AqiHistoryToHour();
            BeanUtil.copyProperties(aqiHour, aqiHistoryToHour);
            aqiHistoryToHours.add(aqiHistoryToHour);
        }
        return aqiHistoryToHours;
    }

    @Override
    public List<AqiHistoryToMonth> getAqiMonthHistory(AirQueryVo airQueryVo) {
        List<AqiHistoryToMonth> aqiHistoryToMonths = new ArrayList<>(0);
        // 获取查询的数据表名称
        List<String> tableNameList = TableNameUtil.tableNameList(TableNameUtil.Aqi_hour, airQueryVo.getStartTime(), airQueryVo.getEndTime());
        // 查询
        for (String name : tableNameList) {
            AqiHistoryToMonth aqiHistoryToMonth = new AqiHistoryToMonth();
            // 时间
            int year = Integer.valueOf(StrUtil.sub(name, 9, 13));
            int month = Integer.valueOf(StrUtil.sub(name, 13, name.length()));
            LocalDateTime startTime = LocalDateTime.of(year, month, 1, 0, 0, 0);
            LocalDateTime endTime = LocalDateTime.of(year, month, startTime.getMonth().maxLength(), 23, 59, 59);
            // 平均值
            AirAvgModel avgToMonth = baseMapper.getAvgToMonth(name, airQueryVo.getDeviceNo(), startTime, endTime);
            if (avgToMonth != null) {
                aqiHistoryToMonth.setPm25(avgToMonth.getPm25Avg().setScale(1, BigDecimal.ROUND_DOWN));
                aqiHistoryToMonth.setPm10(avgToMonth.getPm10Avg().setScale(1, BigDecimal.ROUND_DOWN));
                aqiHistoryToMonth.setSo2(avgToMonth.getSo2Avg().setScale(1, BigDecimal.ROUND_DOWN));
                aqiHistoryToMonth.setNo2(avgToMonth.getNo2Avg().setScale(1, BigDecimal.ROUND_DOWN));
                aqiHistoryToMonth.setCo(avgToMonth.getCoAvg().setScale(1, BigDecimal.ROUND_DOWN));
                aqiHistoryToMonth.setO3(avgToMonth.getO3Avg().setScale(1, BigDecimal.ROUND_DOWN));
                aqiHistoryToMonth.setVoc(avgToMonth.getVocAvg().setScale(1, BigDecimal.ROUND_DOWN));
                // 设备号,时间
                aqiHistoryToMonth.setDeviceNo(airQueryVo.getDeviceNo());
                aqiHistoryToMonth.setDateTime(startTime);
                // 计算AQI
                AqiDay aqiDay = AQIComputedUtil.computedAqiToDay(airQueryVo.getDeviceNo(), startTime, avgToMonth);
                aqiHistoryToMonth.setAqi(aqiDay.getAqi());
                aqiHistoryToMonth.setPollute(aqiDay.getPollute());
                aqiHistoryToMonth.setQuality(aqiDay.getQuality());
                aqiHistoryToMonth.setLevel(aqiDay.getLevel());
                aqiHistoryToMonths.add(aqiHistoryToMonth);
            }
        }
        return aqiHistoryToMonths;
    }

    @Override
    public List<AqiReportToHourModel> getAqiReportToHour(AqiReportVo aqiReportVo) {
        List<AqiReportToHourModel> aqiReportToHourModels = new ArrayList<>(0);
        List<Device> deviceList;
        if ((aqiReportVo.getAddress() == null && aqiReportVo.getLevel() == null) || ("".equals(aqiReportVo.getAddress().trim()) && "".equals(aqiReportVo.getLevel().trim()))) {
            // 查询全部设备列表
            deviceList = deviceService.findDeviceAllToUsername(SecurityUtil.getUsername());
        } else {
            // 按区域查询
            deviceList = deviceService.findDeviceGroupByAddress(aqiReportVo.getAddress(), aqiReportVo.getLevel());
        }
        // 数据表
        String tableName = TableNameUtil.generateTableName(TableNameUtil.Aqi_hour, aqiReportVo.getTime(), ConstUtil.QUERY_DATE);
        // 时间
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(ConstUtil.QUERY_DATE);
        LocalDate localDate = LocalDate.parse(aqiReportVo.getTime(), formatter);
        int hour = Integer.valueOf(aqiReportVo.getHour());
        LocalDateTime start = LocalDateTime.of(localDate.getYear(), localDate.getMonthValue(), localDate.getDayOfMonth(), hour, 0, 0);
        LocalDateTime end = LocalDateTime.of(localDate.getYear(), localDate.getMonthValue(), localDate.getDayOfMonth(), hour, 59, 59);
        for (Device device : deviceList) {
            AqiReportToHourModel hourModel = new AqiReportToHourModel();
            hourModel.setDeviceNo(device.getDeviceNo());
            hourModel.setDeviceName(device.getDeviceName());
            hourModel.setDateTime(start);
            // 查询
            LambdaQueryWrapper<AqiHour> query = Wrappers.<AqiHour>lambdaQuery();
            MybatisPlusConfig.TableName.set(tableName);
            query.eq(AqiHour::getDeviceNo, device.getDeviceNo()).between(AqiHour::getDateTime, start, end);
            AqiHour aqiHour = super.getOne(query);
            if (aqiHour == null) {
                // 添加
                aqiReportToHourModels.add(hourModel);
            } else {
                hourModel.setAqi(aqiHour.getAqi() + "");
                hourModel.setQuality(aqiHour.getQuality());
                hourModel.setLevel(aqiHour.getLevel() + "");
                hourModel.setPollute(aqiHour.getPollute());
                hourModel.setData(aqiHour.getData().toString());
                hourModel.setTips(aqiHour.getTips());
                hourModel.setPm25(aqiHour.getPm25().toString());
                hourModel.setPm10(aqiHour.getPm10().toString());
                hourModel.setSo2(aqiHour.getSo2().toString());
                hourModel.setNo2(aqiHour.getNo2().toString());
                hourModel.setCo(aqiHour.getCo().toString());
                hourModel.setO3(aqiHour.getO3().toString());
                hourModel.setVoc(aqiHour.getVoc().toString());
                hourModel.setPm25Index(aqiHour.getPm25Index() + "");
                hourModel.setPm10Index(aqiHour.getPm10Index() + "");
                hourModel.setSo2Index(aqiHour.getSo2Index() + "");
                hourModel.setNo2Index(aqiHour.getNo2Index() + "");
                hourModel.setCoIndex(aqiHour.getCoIndex() + "");
                hourModel.setO3Index(aqiHour.getO3Index() + "");
                // 添加
                aqiReportToHourModels.add(hourModel);
            }
        }
        return aqiReportToHourModels;
    }

    @Override
    public AqiDataFeatureModel getAqiFeatureData(FeatureVo featureVo) {
        AqiDataFeatureModel aqiDataFeatureModel = new AqiDataFeatureModel();
        // 设备号
        String deviceNo = featureVo.getDeviceNo();
        Device device = deviceService.findOneDeviceToNo(deviceNo);
        if (device == null)
            throw new ServiceException("无法查询设备信息");
        // 时间格式化
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(ConstUtil.QUERY_DATE);
        // 传参时间
        LocalDate argsTime = LocalDate.parse(featureVo.getTime(), formatter);
        if (argsTime.isBefore(LocalDate.of(2019, 12, 1)))
            return new AqiDataFeatureModel();
        // 现在时间
        String nowTableName = TableNameUtil.generateTableName(TableNameUtil.Aqi_hour, argsTime.format(formatter), ConstUtil.QUERY_DATE);
        List<LocalDateTime> nowTimes = DateTimeUtil.queryTimeFormatter(argsTime.format(formatter), argsTime.format(formatter));
        AqiAvgModel nowAvg = baseMapper.getAvgToFeature(nowTableName, deviceNo, nowTimes.get(0), nowTimes.get(1));
        if (BeanUtil.isEmpty(nowAvg))
            nowAvg = new AqiAvgModel();
        // 昨天时间
        LocalDate yeaTime = argsTime.minusDays(1);
        String yesTableName = TableNameUtil.generateTableName(TableNameUtil.Aqi_hour, yeaTime.format(formatter), ConstUtil.QUERY_DATE);
        List<LocalDateTime> yesTimes = DateTimeUtil.queryTimeFormatter(yeaTime.format(formatter), yeaTime.format(formatter));
        AqiAvgModel yesAvg = baseMapper.getAvgToFeature(yesTableName, deviceNo, yesTimes.get(0), yesTimes.get(1));
        if (BeanUtil.isEmpty(yesAvg))
            yesAvg = new AqiAvgModel();
        // 上月今天时间
        LocalDate monTime = argsTime.minusMonths(1);
        String monTableName = TableNameUtil.generateTableName(TableNameUtil.Aqi_hour, monTime.format(formatter), ConstUtil.QUERY_DATE);
        List<LocalDateTime> monTimes = DateTimeUtil.queryTimeFormatter(monTime.format(formatter), monTime.format(formatter));
        AqiAvgModel monAvg = baseMapper.getAvgToFeature(monTableName, deviceNo, monTimes.get(0), monTimes.get(1));
        if (BeanUtil.isEmpty(monAvg))
            monAvg = new AqiAvgModel();
        // 数据同比，环比
        AqiFeatureModel aqiFeatureModel = new AqiFeatureModel();
        aqiFeatureModel.setNowAqi(nowAvg.getAqi());
        aqiFeatureModel.setNowPM25(nowAvg.getPm25().setScale(1, BigDecimal.ROUND_DOWN));
        aqiFeatureModel.setNowPM10(nowAvg.getPm10().setScale(1, BigDecimal.ROUND_DOWN));
        aqiFeatureModel.setNowSo2(nowAvg.getSo2().setScale(1, BigDecimal.ROUND_DOWN));
        aqiFeatureModel.setNowSo2(nowAvg.getNo2().setScale(1, BigDecimal.ROUND_DOWN));
        aqiFeatureModel.setNowCo(nowAvg.getCo().setScale(1, BigDecimal.ROUND_DOWN));
        aqiFeatureModel.setNowO3(nowAvg.getO3().setScale(1, BigDecimal.ROUND_DOWN));
        aqiFeatureModel.setNowVoc(nowAvg.getVoc().setScale(1, BigDecimal.ROUND_DOWN));
        aqiFeatureModel.setYeaAqi(yesAvg.getAqi());
        aqiFeatureModel.setYeaPM25(yesAvg.getPm25().setScale(1, BigDecimal.ROUND_DOWN));
        aqiFeatureModel.setYeaPM10(yesAvg.getPm10().setScale(1, BigDecimal.ROUND_DOWN));
        aqiFeatureModel.setYeaSo2(yesAvg.getSo2().setScale(1, BigDecimal.ROUND_DOWN));
        aqiFeatureModel.setYeaSo2(yesAvg.getNo2().setScale(1, BigDecimal.ROUND_DOWN));
        aqiFeatureModel.setYeaCo(yesAvg.getCo().setScale(1, BigDecimal.ROUND_DOWN));
        aqiFeatureModel.setYeaO3(yesAvg.getO3().setScale(1, BigDecimal.ROUND_DOWN));
        aqiFeatureModel.setYeaVoc(yesAvg.getVoc().setScale(1, BigDecimal.ROUND_DOWN));
        aqiFeatureModel.setMonAqi(monAvg.getAqi());
        aqiFeatureModel.setMonPM25(monAvg.getPm25().setScale(1, BigDecimal.ROUND_DOWN));
        aqiFeatureModel.setMonPM10(monAvg.getPm10().setScale(1, BigDecimal.ROUND_DOWN));
        aqiFeatureModel.setMonSo2(monAvg.getSo2().setScale(1, BigDecimal.ROUND_DOWN));
        aqiFeatureModel.setMonSo2(monAvg.getNo2().setScale(1, BigDecimal.ROUND_DOWN));
        aqiFeatureModel.setMonCo(monAvg.getCo().setScale(1, BigDecimal.ROUND_DOWN));
        aqiFeatureModel.setMonO3(monAvg.getO3().setScale(1, BigDecimal.ROUND_DOWN));
        aqiFeatureModel.setMonVoc(monAvg.getVoc().setScale(1, BigDecimal.ROUND_DOWN));
        aqiFeatureModel.setDeviceNo(device.getDeviceNo());
        aqiFeatureModel.setDeviceName(device.getDeviceName());

        LambdaQueryWrapper<AqiHour> wrapper1 = Wrappers.<AqiHour>lambdaQuery().select(AqiHour::getAqi, AqiHour::getDateTime, AqiHour::getPm25, AqiHour::getPm10, AqiHour::getSo2, AqiHour::getNo2, AqiHour::getCo, AqiHour::getO3, AqiHour::getVoc).eq(AqiHour::getDeviceNo, deviceNo).between(AqiHour::getDateTime, nowTimes.get(0), nowTimes.get(1)).orderByAsc(AqiHour::getDateTime);
        MybatisPlusConfig.TableName.set(nowTableName);
        List<AqiHour> nowList = super.list(wrapper1);
        if (nowList == null)
            nowList = new ArrayList<>(0);
        List<AqiAvgModel> nowData = new ArrayList<>(0);
        for (AqiHour aqiHour : nowList) {
            AqiAvgModel aqiAvgModel = new AqiAvgModel();
            BeanUtil.copyProperties(aqiHour, aqiAvgModel);
            nowData.add(aqiAvgModel);
        }

        LambdaQueryWrapper<AqiHour> wrapper2 = Wrappers.<AqiHour>lambdaQuery().select(AqiHour::getAqi, AqiHour::getDateTime, AqiHour::getPm25, AqiHour::getPm10, AqiHour::getSo2, AqiHour::getNo2, AqiHour::getCo, AqiHour::getO3, AqiHour::getVoc).eq(AqiHour::getDeviceNo, deviceNo).between(AqiHour::getDateTime, yesTimes.get(0), yesTimes.get(1)).orderByAsc(AqiHour::getDateTime);
        MybatisPlusConfig.TableName.set(yesTableName);
        List<AqiHour> yesList = super.list(wrapper2);
        if (yesList == null)
            yesList = new ArrayList<>(0);
        List<AqiAvgModel> yesData = new ArrayList<>(0);
        for (AqiHour aqiHour : yesList) {
            AqiAvgModel aqiAvgModel = new AqiAvgModel();
            BeanUtil.copyProperties(aqiHour, aqiAvgModel);
            yesData.add(aqiAvgModel);
        }

        LambdaQueryWrapper<AqiHour> wrapper3 = Wrappers.<AqiHour>lambdaQuery().select(AqiHour::getAqi, AqiHour::getDateTime, AqiHour::getPm25, AqiHour::getPm10, AqiHour::getSo2, AqiHour::getNo2, AqiHour::getCo, AqiHour::getO3, AqiHour::getVoc).eq(AqiHour::getDeviceNo, deviceNo).between(AqiHour::getDateTime, monTimes.get(0), monTimes.get(1)).orderByAsc(AqiHour::getDateTime);
        MybatisPlusConfig.TableName.set(monTableName);
        List<AqiHour> monList = super.list(wrapper3);
        if (monList == null)
            monList = new ArrayList<>(0);
        List<AqiAvgModel> monData = new ArrayList<>(0);
        for (AqiHour aqiHour : monList) {
            AqiAvgModel aqiAvgModel = new AqiAvgModel();
            BeanUtil.copyProperties(aqiHour, aqiAvgModel);
            monData.add(aqiAvgModel);
        }

        aqiDataFeatureModel.setAqiFeatureModel(aqiFeatureModel);
        aqiDataFeatureModel.setNowAqiAvgModels(nowData);
        aqiDataFeatureModel.setYeaAqiAvgModels(yesData);
        aqiDataFeatureModel.setMonAqiAvgModels(monData);

        return aqiDataFeatureModel;
    }

    @Override
    public AqiCompareDataModel getAqiHourToCompare(AqiCompareVo aqiCompareVo) {
        // 设备号
        String deviceNo1 = aqiCompareVo.getDev1();
        String deviceNo2 = aqiCompareVo.getDev2();
        // 获取查询数据表
        List<String> nameList = TableNameUtil.tableNameList(TableNameUtil.Aqi_hour, aqiCompareVo.getStartTime(), aqiCompareVo.getEndTime());
        // 查询时间
        List<LocalDateTime> localDateTimes = DateTimeUtil.queryTimeFormatter(aqiCompareVo.getStartTime(), aqiCompareVo.getEndTime());

        List<AqiHour> first = new ArrayList<>(0);
        List<AqiHour> second = new ArrayList<>(0);
        // 查询
        LambdaQueryWrapper<AqiHour> wrapper1 = Wrappers.<AqiHour>lambdaQuery().select(AqiHour::getDeviceNo, AqiHour::getAqi, AqiHour::getDateTime, AqiHour::getPm25, AqiHour::getPm10, AqiHour::getSo2, AqiHour::getNo2, AqiHour::getCo, AqiHour::getO3, AqiHour::getVoc);
        LambdaQueryWrapper<AqiHour> wrapper2 = Wrappers.<AqiHour>lambdaQuery().select(AqiHour::getDeviceNo, AqiHour::getAqi, AqiHour::getDateTime, AqiHour::getPm25, AqiHour::getPm10, AqiHour::getSo2, AqiHour::getNo2, AqiHour::getCo, AqiHour::getO3, AqiHour::getVoc);
        for (String name : nameList) {
            MybatisPlusConfig.TableName.set(name);
            wrapper1.eq(AqiHour::getDeviceNo, deviceNo1).between(AqiHour::getDateTime, localDateTimes.get(0), localDateTimes.get(1));
            List<AqiHour> list1 = super.list(wrapper1);
            if (list1 != null)
                first.addAll(list1);
            MybatisPlusConfig.TableName.set(name);
            wrapper2.eq(AqiHour::getDeviceNo, deviceNo2).between(AqiHour::getDateTime, localDateTimes.get(0), localDateTimes.get(1));
            List<AqiHour> list2 = super.list(wrapper2);
            if (list2 != null)
                second.addAll(list2);
        }
        List<AqiHistoryToHour> d1 = new ArrayList<>(0);
        List<AqiHistoryToHour> d2 = new ArrayList<>(0);
        for (AqiHour aqiHour : first) {
            AqiHistoryToHour hour = new AqiHistoryToHour();
            BeanUtil.copyProperties(aqiHour, hour);
            d1.add(hour);
        }
        for (AqiHour aqiHour : second) {
            AqiHistoryToHour hour = new AqiHistoryToHour();
            BeanUtil.copyProperties(aqiHour, hour);
            d2.add(hour);
        }
        AqiCompareDataModel aqiCompareDataModel = new AqiCompareDataModel();
        aqiCompareDataModel.setFirstDeviceData(d1);
        aqiCompareDataModel.setSecondDeviceData(d2);
        return aqiCompareDataModel;
    }

    @Override
    public AqiCompareDataModel getAqiMonthToCompare(AqiCompareVo aqiCompareVo) {
        // 月份数据
        AirQueryVo airQueryVo1 = new AirQueryVo(aqiCompareVo.getDev1(), aqiCompareVo.getStartTime(), aqiCompareVo.getEndTime());
        AirQueryVo airQueryVo2 = new AirQueryVo(aqiCompareVo.getDev2(), aqiCompareVo.getStartTime(), aqiCompareVo.getEndTime());

        List<AqiHistoryToMonth> aqiMonthHistory = this.getAqiMonthHistory(airQueryVo1);
        List<AqiHistoryToMonth> aqiMonthHistory1 = this.getAqiMonthHistory(airQueryVo2);

        List<AqiHistoryToHour> d1 = new ArrayList<>(0);
        List<AqiHistoryToHour> d2 = new ArrayList<>(0);
        for (AqiHistoryToMonth aqiHistoryToMonth : aqiMonthHistory) {
            AqiHistoryToHour hour = new AqiHistoryToHour();
            BeanUtil.copyProperties(aqiHistoryToMonth, hour);
            d1.add(hour);
        }
        for (AqiHistoryToMonth aqiHistoryToMonth : aqiMonthHistory1) {
            AqiHistoryToHour hour = new AqiHistoryToHour();
            BeanUtil.copyProperties(aqiHistoryToMonth, hour);
            d2.add(hour);
        }
        AqiCompareDataModel aqiCompareDataModel = new AqiCompareDataModel();
        aqiCompareDataModel.setFirstDeviceData(d1);
        aqiCompareDataModel.setSecondDeviceData(d2);
        return aqiCompareDataModel;
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
            String tableName = TableNameUtil.Aqi_hour + "_" + tempTime.getYear() + ((tempTime.getMonthValue() < 10) ? "0" + tempTime.getMonthValue() : "" + tempTime.getMonthValue());
            // 查询数据
            List<SensorMapperModel> sensorModel = baseMapper.getSensorModel(tableName, deviceNos, sensor, tempTime);
            if (sensorModel == null || sensorModel.size() <= 0) {
                tempTime = tempTime.plusHours(1);
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

            tempTime = tempTime.plusHours(1);
        }
        return mapResultModel;
    }

    @Override
    public List<AqiSensorModel> getAqiSensorModel(String deviceNo, String type, String sensor) {
        List<AqiSensorModel> aqiSensorModels = new ArrayList<>(0);
        // 时间
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime endTime = LocalDateTime.of(now.getYear(), now.getMonthValue(), now.getDayOfMonth(), now.getHour(), 0, 0);
        LocalDateTime startTime = endTime.minusHours(23);
        // 获取表名
        List<String> nameList = TableNameUtil.tableNameList(TableNameUtil.Aqi_hour, startTime, endTime);
        for (String tableName : nameList) {
            List<AqiSensorModel> history = baseMapper.getSensorHistory(tableName, deviceNo, startTime, endTime, sensor);
            aqiSensorModels.addAll(history);
        }

        List<AqiSensorModel> res = new ArrayList<>(0);
        LocalDateTime temp = startTime;
        for (int i = 0; i < 24; i++) {
            AqiSensorModel aqiDataModel = new AqiSensorModel();
            LocalDateTime finalTemp = temp;
            List<AqiSensorModel> modelList = aqiSensorModels.stream().filter(aqiSensorModel -> aqiSensorModel.getDateTime().isEqual(finalTemp)).collect(Collectors.toList());
            if (modelList.size() == 1) {
                AqiSensorModel model = modelList.get(0);
                aqiDataModel.setDateTime(model.getDateTime());
                aqiDataModel.setNumber(model.getNumber());
            } else {
                aqiDataModel.setDateTime(temp);
                aqiDataModel.setNumber(new BigDecimal(0));
            }
            res.add(aqiDataModel);
            // 时间修改
            temp = temp.plusHours(1);
        }
        return res;
    }

    @Override
    public List<AqiSensorRankModel> getAqiSensorRank(String order, String type, String sensor) {
        List<AqiSensorRankModel> aqiSensorRankModels = new ArrayList<>(0);
        // 当前用户设备列表
        String username = SecurityUtil.getUsername();
        List<Device> deviceList = deviceService.findDeviceAllToUsername(username);
        if (deviceList.size() <= 0)
            return aqiSensorRankModels;
        // 时间格式化
        LocalDateTime dateTime = LocalDateTime.now();
        LocalDateTime startTime = null;
        LocalDateTime endTime = null;
        if (Integer.parseInt(type) == 1) {
            // 实时
            startTime = LocalDateTime.of(dateTime.getYear(), dateTime.getMonthValue(), dateTime.getDayOfMonth(), dateTime.getHour(), 0, 0);
            endTime = LocalDateTime.of(dateTime.getYear(), dateTime.getMonthValue(), dateTime.getDayOfMonth(), dateTime.getHour(), 59, 59);
        } else if (Integer.parseInt(type) == 2) {
            // 昨日
            dateTime = dateTime.minusDays(1);
            startTime = LocalDateTime.of(dateTime.getYear(), dateTime.getMonthValue(), dateTime.getDayOfMonth(), 0, 0, 0);
            endTime = LocalDateTime.of(dateTime.getYear(), dateTime.getMonthValue(), dateTime.getDayOfMonth(), 23, 59, 59);
        } else {
            // 当月
            startTime = LocalDateTime.of(dateTime.getYear(), dateTime.getMonthValue(), 1, 0, 0, 0);
            endTime = LocalDateTime.of(dateTime.getYear(), dateTime.getMonthValue(), dateTime.getMonth().maxLength(), 23, 59, 59);
        }
        // 设备号列表
        List<String> deviceNos = new ArrayList<>();
        deviceList.forEach(device -> deviceNos.add(device.getDeviceNo()));
        // 表名
        LocalDate date = startTime.toLocalDate();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(ConstUtil.QUERY_DATE);
        String tableName = TableNameUtil.generateTableName(TableNameUtil.Aqi_hour, date.format(formatter),
                ConstUtil.QUERY_DATE);
        // 查询
        List<SensorMapperModel> aqiSensorRank = baseMapper.getAqiSensorRank(tableName, deviceNos, sensor, startTime, endTime);
        for (Device device : deviceList) {
            AqiSensorRankModel aqiSensorRankModel = new AqiSensorRankModel();
            aqiSensorRankModel.setDeviceNo(device.getDeviceNo());
            aqiSensorRankModel.setDeviceName(device.getDeviceName());
            List<SensorMapperModel> list = aqiSensorRank.stream().filter(aqiRank -> aqiRank.getDeviceNo().equals(device.getDeviceNo())).collect(Collectors.toList());
            if (list.size() == 1) {
                aqiSensorRankModel.setNumber(new BigDecimal(list.get(0).getNumber()));
            } else {
                aqiSensorRankModel.setNumber(new BigDecimal(0));
            }
            aqiSensorRankModels.add(aqiSensorRankModel);
        }
        List<AqiSensorRankModel> modelList = new ArrayList<>(0);
        // 排名
        if ("asc".equals(order)) {
            // 升序
            modelList = aqiSensorRankModels.stream().sorted(Comparator.comparing(AqiSensorRankModel::getNumber)).collect(Collectors.toList());
        }
        if ("desc".equals(order)) {
            // 降序
            modelList = aqiSensorRankModels.stream().sorted(Comparator.comparing(AqiSensorRankModel::getNumber).reversed()).collect(Collectors.toList());
        }
        return modelList;
    }
}
