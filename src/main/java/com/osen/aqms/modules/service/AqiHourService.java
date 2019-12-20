package com.osen.aqms.modules.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.osen.aqms.common.model.AqiDataModel;
import com.osen.aqms.common.model.AqiHistoryToHour;
import com.osen.aqms.common.model.AqiHistoryToMonth;
import com.osen.aqms.common.model.AqiReportToHourModel;
import com.osen.aqms.common.requestVo.AirQueryVo;
import com.osen.aqms.common.requestVo.AqiReportVo;
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
}
