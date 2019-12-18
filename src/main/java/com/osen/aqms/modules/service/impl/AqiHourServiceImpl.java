package com.osen.aqms.modules.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.osen.aqms.common.config.MybatisPlusConfig;
import com.osen.aqms.common.model.AqiDataModel;
import com.osen.aqms.common.utils.TableNameUtil;
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
}
