package com.osen.aqms.modules.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.osen.aqms.common.model.*;
import com.osen.aqms.common.requestVo.AirQueryVo;
import com.osen.aqms.common.requestVo.AqiCompareVo;
import com.osen.aqms.common.requestVo.AqiReportVo;
import com.osen.aqms.common.requestVo.FeatureVo;
import com.osen.aqms.modules.entity.data.AqiHour;

import java.util.List;

/**
 * User: PangYi
 * Date: 2019-11-30
 * Time: 10:47
 * Description:
 */
public interface AqiHourService extends IService<AqiHour> {

    /**
     * 根据设备号获取近24小时AQI值
     *
     * @param deviceNo 设备号
     * @return 信息
     */
    List<AqiDataModel> getAqi24HToDeviceNo(String deviceNo);

    /**
     * 根据设备号AQI小时历史数据
     *
     * @param airQueryVo 请求体
     * @return 信息
     */
    List<AqiHistoryToHour> getAqiHourHistory(AirQueryVo airQueryVo);

    /**
     * 根据设备号AQI小时历史数据
     *
     * @param airQueryVo 请求体
     * @return 信息
     */
    List<AqiHistoryToMonth> getAqiMonthHistory(AirQueryVo airQueryVo);

    /**
     * 获取AQI的小时报表数据
     *
     * @param aqiReportVo 请求体
     * @return 信息
     */
    List<AqiReportToHourModel> getAqiReportToHour(AqiReportVo aqiReportVo);

    /**
     * 获取AQI同比，环比分析数据
     *
     * @param featureVo 请求体
     * @return 信息
     */
    AqiDataFeatureModel getAqiFeatureData(FeatureVo featureVo);

    /**
     * 获取小时设备对比分析数据
     *
     * @param aqiCompareVo 请求体
     * @return 信息
     */
    AqiCompareDataModel getAqiHourToCompare(AqiCompareVo aqiCompareVo);

    /**
     * 获取月份设备对比分析数据
     *
     * @param aqiCompareVo 请求体
     * @return 信息
     */
    AqiCompareDataModel getAqiMonthToCompare(AqiCompareVo aqiCompareVo);
}
