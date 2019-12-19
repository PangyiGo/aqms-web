package com.osen.aqms.web.data_aqi;

import com.osen.aqms.common.model.AqiHistoryToDay;
import com.osen.aqms.common.model.AqiHistoryToHour;
import com.osen.aqms.common.model.AqiHistoryToMonth;
import com.osen.aqms.common.requestVo.AirQueryVo;
import com.osen.aqms.common.result.RestResult;
import com.osen.aqms.common.utils.RestResultUtil;
import com.osen.aqms.modules.service.AqiDayService;
import com.osen.aqms.modules.service.AqiHourService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * User: PangYi
 * Date: 2019-12-19
 * Time: 13:54
 * Description: 空气参数历史数据查询
 */
@RestController
@RequestMapping("${restful.prefix}")
@Slf4j
public class AqiDataHistoryController {

    @Autowired
    private AqiHourService aqiHourService;

    @Autowired
    private AqiDayService aqiDayService;

    /**
     * 根据设备号AQI小时历史数据
     *
     * @param airQueryVo 请求体
     * @return 信息
     */
    @PostMapping("/aqiHistory/hour")
    public RestResult getAqiHourHistory(@RequestBody AirQueryVo airQueryVo) {
        List<AqiHistoryToHour> aqiHourHistory = aqiHourService.getAqiHourHistory(airQueryVo);
        return RestResultUtil.success(aqiHourHistory);
    }

    /**
     * 根据设备号AQI日历史数据
     *
     * @param airQueryVo 请求体
     * @return 信息
     */
    @PostMapping("/aqiHistory/day")
    public RestResult getAqiDayHistory(@RequestBody AirQueryVo airQueryVo) {
        List<AqiHistoryToDay> aqiDayHistory = aqiDayService.getAqiDayHistory(airQueryVo);
        return RestResultUtil.success(aqiDayHistory);
    }

    /**
     * 根据设备号AQI月平均历史数据
     *
     * @param airQueryVo 请求体
     * @return 信息
     */
    @PostMapping("/aqiHistory/month")
    public RestResult getAqiMonthHistory(@RequestBody AirQueryVo airQueryVo) {
        List<AqiHistoryToMonth> aqiMonthHistory = aqiHourService.getAqiMonthHistory(airQueryVo);
        return RestResultUtil.success(aqiMonthHistory);
    }

}
