package com.osen.aqms.modules.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.osen.aqms.common.config.MybatisPlusConfig;
import com.osen.aqms.common.enums.AirSensor;
import com.osen.aqms.common.model.AirDataModel;
import com.osen.aqms.common.utils.TableNameUtil;
import com.osen.aqms.modules.entity.data.AirHistory;
import com.osen.aqms.modules.mapper.data.AirHistoryMapper;
import com.osen.aqms.modules.service.AirHistoryService;
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
public class AirHistoryServiceImpl extends ServiceImpl<AirHistoryMapper, AirHistory> implements AirHistoryService {

    @Override
    public List<AirDataModel> getAirDataToSensor(String deviceNO, String sensor) {
        List<AirDataModel> airDataModels = new ArrayList<>(0);
        // 查询时间
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = startTime.minusHours(12);
        // 基本数据表
        List<String> nameList = TableNameUtil.tableNameList(TableNameUtil.Air_history, startTime, endTime);
        if (nameList.size() <= 0)
            return airDataModels;
        // 参数因子
        LambdaQueryWrapper<AirHistory> query = Wrappers.<AirHistory>lambdaQuery();
        if (AirSensor.PM25.getName().equals(sensor)) {
            query.select(AirHistory::getDateTime, AirHistory::getPm25).eq(AirHistory::getDeviceNo, deviceNO).between(AirHistory::getDateTime, startTime, endTime);
            for (String name : nameList) {
                MybatisPlusConfig.TableName.set(name);
                List<AirHistory> histories = super.list(query);
                if (histories != null) {
                    for (AirHistory history : histories) {
                        AirDataModel airDataModel = new AirDataModel();
                        airDataModel.setData(history.getPm25());
                        airDataModel.setDateTime(history.getDateTime());
                    }
                }
            }
        }
        if (AirSensor.PM10.getName().equals(sensor)) {
            query.select(AirHistory::getDateTime, AirHistory::getPm10).eq(AirHistory::getDeviceNo, deviceNO).between(AirHistory::getDateTime, startTime, endTime);
            List<AirHistory> histories = super.list(query);
            if (histories != null) {
                for (AirHistory history : histories) {
                    AirDataModel airDataModel = new AirDataModel();
                    airDataModel.setData(history.getPm10());
                    airDataModel.setDateTime(history.getDateTime());
                }
            }
        }
        if (AirSensor.SO2.getName().equals(sensor)) {
            query.select(AirHistory::getDateTime, AirHistory::getSo2).eq(AirHistory::getDeviceNo, deviceNO).between(AirHistory::getDateTime, startTime, endTime);
            List<AirHistory> histories = super.list(query);
            if (histories != null) {
                for (AirHistory history : histories) {
                    AirDataModel airDataModel = new AirDataModel();
                    airDataModel.setData(history.getSo2());
                    airDataModel.setDateTime(history.getDateTime());
                }
            }
        }
        if (AirSensor.NO2.getName().equals(sensor)) {
            query.select(AirHistory::getDateTime, AirHistory::getNo2).eq(AirHistory::getDeviceNo, deviceNO).between(AirHistory::getDateTime, startTime, endTime);
            List<AirHistory> histories = super.list(query);
            if (histories != null) {
                for (AirHistory history : histories) {
                    AirDataModel airDataModel = new AirDataModel();
                    airDataModel.setData(history.getNo2());
                    airDataModel.setDateTime(history.getDateTime());
                }
            }
        }
        if (AirSensor.CO.getName().equals(sensor)) {
            query.select(AirHistory::getDateTime, AirHistory::getCo).eq(AirHistory::getDeviceNo, deviceNO).between(AirHistory::getDateTime, startTime, endTime);
            List<AirHistory> histories = super.list(query);
            if (histories != null) {
                for (AirHistory history : histories) {
                    AirDataModel airDataModel = new AirDataModel();
                    airDataModel.setData(history.getCo());
                    airDataModel.setDateTime(history.getDateTime());
                }
            }
        }
        if (AirSensor.O3.getName().equals(sensor)) {
            query.select(AirHistory::getDateTime, AirHistory::getO3).eq(AirHistory::getDeviceNo, deviceNO).between(AirHistory::getDateTime, startTime, endTime);
            List<AirHistory> histories = super.list(query);
            if (histories != null) {
                for (AirHistory history : histories) {
                    AirDataModel airDataModel = new AirDataModel();
                    airDataModel.setData(history.getO3());
                    airDataModel.setDateTime(history.getDateTime());
                }
            }
        }
        if (AirSensor.VOC.getName().equals(sensor)) {
            query.select(AirHistory::getDateTime, AirHistory::getVoc).eq(AirHistory::getDeviceNo, deviceNO).between(AirHistory::getDateTime, startTime, endTime);
            List<AirHistory> histories = super.list(query);
            if (histories != null) {
                for (AirHistory history : histories) {
                    AirDataModel airDataModel = new AirDataModel();
                    airDataModel.setData(history.getVoc());
                    airDataModel.setDateTime(history.getDateTime());
                }
            }
        }
        return airDataModels;
    }
}
