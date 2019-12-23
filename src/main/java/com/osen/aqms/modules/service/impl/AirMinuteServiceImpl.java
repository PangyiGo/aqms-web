package com.osen.aqms.modules.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.osen.aqms.common.config.MybatisPlusConfig;
import com.osen.aqms.common.model.AirQueryDataModel;
import com.osen.aqms.common.requestVo.AirExportVo;
import com.osen.aqms.common.requestVo.AirQueryVo;
import com.osen.aqms.common.utils.DateTimeUtil;
import com.osen.aqms.common.utils.TableNameUtil;
import com.osen.aqms.modules.entity.data.AirMinute;
import com.osen.aqms.modules.entity.system.Device;
import com.osen.aqms.modules.mapper.data.AirMinuteMapper;
import com.osen.aqms.modules.service.AirMinuteService;
import com.osen.aqms.modules.service.DeviceService;
import com.osen.aqms.web.data_air.utils.AirSensorHistoryExport;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: PangYi
 * Date: 2019-11-30
 * Time: 10:52
 * Description:
 */
@Service
public class AirMinuteServiceImpl extends ServiceImpl<AirMinuteMapper, AirMinute> implements AirMinuteService {

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private AirSensorHistoryExport airSensorHistoryExport;

    @Override
    public List<AirQueryDataModel> getAirMinuteHistory(AirQueryVo airQueryVo) {
        List<AirQueryDataModel> airQueryDataModels = new ArrayList<>(0);
        // 获取查询数据表
        List<String> tableNameList = TableNameUtil.tableNameList(TableNameUtil.Air_minute, airQueryVo.getStartTime(), airQueryVo.getEndTime());
        // 时间格式换
        List<LocalDateTime> dateTimes = DateTimeUtil.queryTimeFormatter(airQueryVo.getStartTime(), airQueryVo.getEndTime());
        // 查询
        List<AirMinute> historyList = new ArrayList<>(0);
        LambdaQueryWrapper<AirMinute> query = Wrappers.<AirMinute>lambdaQuery();
        for (String name : tableNameList) {
            MybatisPlusConfig.TableName.set(name);
            query.select(AirMinute::getDeviceNo, AirMinute::getPm25, AirMinute::getPm10, AirMinute::getSo2, AirMinute::getNo2, AirMinute::getCo, AirMinute::getO3, AirMinute::getVoc, AirMinute::getDateTime);
            query.eq(AirMinute::getDeviceNo, airQueryVo.getDeviceNo()).between(AirMinute::getDateTime, dateTimes.get(0), dateTimes.get(1));
            // 添加
            historyList.addAll(super.list(query));
        }
        for (AirMinute airHistory : historyList) {
            AirQueryDataModel airQueryDataModel = new AirQueryDataModel();
            BeanUtil.copyProperties(airHistory, airQueryDataModel);
            airQueryDataModels.add(airQueryDataModel);
        }
        return airQueryDataModels;
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
            List<AirQueryDataModel> airRealtimeHistory = this.getAirMinuteHistory(airQueryVo);
            // 设备信息
            Device device = deviceService.findOneDeviceToNo(deviceNo);
            String name = device.getDeviceNo() + "-" + device.getDeviceName();
            listMap.put(name, airRealtimeHistory);
        }
        return airSensorHistoryExport.exportToAirHistory(listMap);
    }
}
