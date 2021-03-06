package com.osen.aqms.modules.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.osen.aqms.common.model.*;
import com.osen.aqms.common.requestVo.*;
import com.osen.aqms.modules.entity.data.AirHistory;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.util.List;
import java.util.Map;

/**
 * User: PangYi
 * Date: 2019-11-30
 * Time: 10:47
 * Description:
 */
public interface AirHistoryService extends IService<AirHistory> {

    /**
     * 获取指定监控参数因子近12小时的历史数据
     *
     * @param deviceNo 设备号
     * @param sensor   参数因子
     * @return 信息
     */
    List<AirDataModel> getAirDataToSensor(String deviceNo, String sensor);

    /**
     * 根据设备号查询最新的实时数据
     *
     * @param deviceNo 设备号
     * @return 信息
     */
    AirRealTimeModel getAirRealtime(String deviceNo);

    /**
     * 根据区域查询设备列表的空气站实时数据
     *
     * @param params 可指定区域，或默认查询全部
     * @return 信息
     */
    List<AqiDataToMapModel> getAirRealtimeList(Map<String, Object> params);

    /**
     * 查询当前用户的空气站监测实时数据排行
     *
     * @param airRankVo 请求体
     * @return 信息
     */
    List<AqiDataToMapModel> getAirRankToRealtime(AirRankVo airRankVo);

    /**
     * 查询当前用户的空气站监测历史数据排行
     *
     * @param airRankVo 请求体
     * @return 信息
     */
    List<AqiDataToMapModel> getAirRankToHistory(AirRankVo airRankVo);

    /**
     * 查询设备的空气空气参数实时历史数据
     *
     * @param airQueryVo 请求体
     * @return 信息
     */
    List<AirQueryDataModel> getAirRealtimeHistory(AirQueryVo airQueryVo);

    /**
     * 获取空气站参数日统计报表
     *
     * @param airAccordVo 请求体
     * @return 信息
     */
    List<AirAccordModel> getAirAccordToDay(AirAccordVo airAccordVo);

    /**
     * 获取空气站参数月统计报表
     *
     * @param airAccordVo 请求体
     * @return 信息
     */
    List<AirAccordModel> getAirAccordToMonth(AirAccordVo airAccordVo);

    /**
     * 获取设备数据总览
     *
     * @param airMonitorVo 请求体
     * @return 信息
     */
    AirMonitorModel getAirMonitor(AirMonitorVo airMonitorVo);

    /**
     * 空气站实时数据报表导出
     *
     * @param airExportVo 请求体
     * @return 信息
     */
    HSSFWorkbook getAirSensorToExport(AirExportVo airExportVo);

    /**
     * 根据省份分组获取实时数据的各个参数最大值
     *
     * @param addressVo 请求体
     * @return 信息
     */
    AirSensorMaxModel getAirSensorMax(AddressVo addressVo);
}
