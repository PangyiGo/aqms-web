package com.osen.aqms.modules.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.osen.aqms.common.model.*;
import com.osen.aqms.common.requestVo.AirQueryVo;
import com.osen.aqms.common.requestVo.AqiCompareVo;
import com.osen.aqms.common.requestVo.AqiReportVo;
import com.osen.aqms.common.requestVo.PolluteMapVo;
import com.osen.aqms.modules.entity.data.AqiDay;

import java.util.List;

/**
 * User: PangYi
 * Date: 2019-11-30
 * Time: 10:47
 * Description:
 */
public interface AqiDayService extends IService<AqiDay> {

    /**
     * 根据设备号AQI日历史数据
     *
     * @param airQueryVo 请求体
     * @return 信息
     */
    List<AqiHistoryToDay> getAqiDayHistory(AirQueryVo airQueryVo);

    /**
     * 获取AQI的日报表数据
     *
     * @param aqiReportVo 请求体
     * @return 信息
     */
    List<AqiReportToDayModel> getAqiReportToDay(AqiReportVo aqiReportVo);

    /**
     * 获取小时设备对比分析数据
     *
     * @param aqiCompareVo 请求体
     * @return 信息
     */
    AqiCompareDataModel getAqiDayToCompare(AqiCompareVo aqiCompareVo);

    /**
     * 计算当月的空气比天数
     *
     * @param airQueryVo 请求体
     * @return 信息
     */
    LevelDayModel getLevelNumber(AirQueryVo airQueryVo);

    /**
     * 获取污染物地图数据
     *
     * @param polluteMapVo 请求体
     * @param sensor       参数因子
     * @return 信息
     */
    List<PolluteMapResultModel> getSensorData(PolluteMapVo polluteMapVo, String sensor);

    /**
     * 获取七天或三十天的污染物历史数据
     *
     * @param deviceNo 设备号
     * @param type     类型 2表示七天，3表示30天
     * @param sensor   参数因子
     * @return 信息
     */
    List<AqiSensorDayModel> getAqiSensorHistory(String deviceNo, String type, String sensor);

    /**
     * 根据设备号获取当月报警次数
     *
     * @param deviceNo 设备号
     * @return 信息
     */
    AqiViewModel getAqiNumber(String deviceNo);
}
