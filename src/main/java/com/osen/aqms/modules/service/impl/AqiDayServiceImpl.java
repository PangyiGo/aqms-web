package com.osen.aqms.modules.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.osen.aqms.common.config.MybatisPlusConfig;
import com.osen.aqms.common.model.AqiCompareDataModel;
import com.osen.aqms.common.model.AqiHistoryToDay;
import com.osen.aqms.common.model.AqiHistoryToHour;
import com.osen.aqms.common.model.AqiReportToDayModel;
import com.osen.aqms.common.requestVo.AirQueryVo;
import com.osen.aqms.common.requestVo.AqiCompareVo;
import com.osen.aqms.common.requestVo.AqiReportVo;
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

import java.time.LocalDate;
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
        LambdaQueryWrapper<AqiDay> wrapper1 = Wrappers.<AqiDay>lambdaQuery().select(AqiDay::getDeviceNo,
                AqiDay::getAqi, AqiDay::getDateTime, AqiDay::getPm25, AqiDay::getPm10, AqiDay::getSo2, AqiDay::getNo2, AqiDay::getCo, AqiDay::getO3, AqiDay::getVoc);
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
}
