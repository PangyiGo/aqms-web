package com.osen.aqms.modules.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.osen.aqms.common.model.AqiHistoryToDay;
import com.osen.aqms.common.model.AqiReportToDayModel;
import com.osen.aqms.common.requestVo.AirQueryVo;
import com.osen.aqms.common.requestVo.AqiReportVo;
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
}
