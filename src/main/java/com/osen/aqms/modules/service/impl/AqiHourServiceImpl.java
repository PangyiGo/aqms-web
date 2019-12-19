package com.osen.aqms.modules.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.osen.aqms.common.config.MybatisPlusConfig;
import com.osen.aqms.common.model.AirAvgModel;
import com.osen.aqms.common.model.AqiDataModel;
import com.osen.aqms.common.model.AqiHistoryToHour;
import com.osen.aqms.common.model.AqiHistoryToMonth;
import com.osen.aqms.common.requestVo.AirQueryVo;
import com.osen.aqms.common.utils.AQIComputedUtil;
import com.osen.aqms.common.utils.DateTimeUtil;
import com.osen.aqms.common.utils.TableNameUtil;
import com.osen.aqms.modules.entity.data.AqiDay;
import com.osen.aqms.modules.entity.data.AqiHour;
import com.osen.aqms.modules.mapper.data.AqiHourMapper;
import com.osen.aqms.modules.service.AqiHourService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * User: PangYi
 * Date: 2019-11-30
 * Time: 10:52
 * Description:
 */
@Service
public class AqiHourServiceImpl extends ServiceImpl<AqiHourMapper, AqiHour> implements AqiHourService {

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
            aqiHistoryToMonth.setPm25(avgToMonth.getPm25Avg());
            aqiHistoryToMonth.setPm10(avgToMonth.getPm10Avg());
            aqiHistoryToMonth.setSo2(avgToMonth.getSo2Avg());
            aqiHistoryToMonth.setNo2(avgToMonth.getNo2Avg());
            aqiHistoryToMonth.setCo(avgToMonth.getCoAvg());
            aqiHistoryToMonth.setO3(avgToMonth.getO3Avg());
            aqiHistoryToMonth.setVoc(avgToMonth.getVocAvg());
            // 设备号,时间
            aqiHistoryToMonth.setDeviceNo(airQueryVo.getDeviceNo());
            aqiHistoryToMonth.setDateTime(startTime);
            // 计算AQI
            AqiDay aqiDay = AQIComputedUtil.computedAqiToDay(airQueryVo.getDeviceNo(), startTime, avgToMonth);
            aqiDay.setAqi(aqiDay.getAqi());
            aqiDay.setPollute(aqiDay.getPollute());
            aqiDay.setQuality(aqiDay.getQuality());
            aqiDay.setLevel(aqiDay.getLevel());

            aqiHistoryToMonths.add(aqiHistoryToMonth);
        }
        return aqiHistoryToMonths;
    }
}
