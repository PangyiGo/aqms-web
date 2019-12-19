package com.osen.aqms.modules.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.osen.aqms.common.config.MybatisPlusConfig;
import com.osen.aqms.common.model.AqiHistoryToDay;
import com.osen.aqms.common.requestVo.AirQueryVo;
import com.osen.aqms.common.utils.DateTimeUtil;
import com.osen.aqms.common.utils.TableNameUtil;
import com.osen.aqms.modules.entity.data.AqiDay;
import com.osen.aqms.modules.mapper.data.AqiDayMapper;
import com.osen.aqms.modules.service.AqiDayService;
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
public class AqiDayServiceImpl extends ServiceImpl<AqiDayMapper, AqiDay> implements AqiDayService {

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
}
