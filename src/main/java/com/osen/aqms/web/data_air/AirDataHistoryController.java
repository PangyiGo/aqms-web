package com.osen.aqms.web.data_air;

import com.osen.aqms.common.model.AirQueryDataModel;
import com.osen.aqms.common.requestVo.AirQueryVo;
import com.osen.aqms.common.result.RestResult;
import com.osen.aqms.common.utils.RestResultUtil;
import com.osen.aqms.modules.service.AirDayService;
import com.osen.aqms.modules.service.AirHistoryService;
import com.osen.aqms.modules.service.AirHourService;
import com.osen.aqms.modules.service.AirMinuteService;
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
public class AirDataHistoryController {

    @Autowired
    private AirHistoryService airHistoryService;

    @Autowired
    private AirMinuteService airMinuteService;

    @Autowired
    private AirHourService airHourService;

    @Autowired
    private AirDayService airDayService;

    /**
     * 根据设备号查询空气质量参数实时历史数据
     *
     * @param airQueryVo 请求体
     * @return 信息
     */
    @PostMapping("/airSensor/history")
    public RestResult getAirRealtimeHistory(@RequestBody AirQueryVo airQueryVo) {
        List<AirQueryDataModel> airRealtimeHistory = airHistoryService.getAirRealtimeHistory(airQueryVo);
        return RestResultUtil.success(airRealtimeHistory);
    }

    /**
     * 根据设备号查询空气质量参数分钟历史数据
     *
     * @param airQueryVo 请求体
     * @return 信息
     */
    @PostMapping("/airSensor/minute")
    public RestResult getAirMinuteHistory(@RequestBody AirQueryVo airQueryVo) {
        List<AirQueryDataModel> airMinuteHistory = airMinuteService.getAirMinuteHistory(airQueryVo);
        return RestResultUtil.success(airMinuteHistory);
    }

    /**
     * 根据设备号查询空气质量参数小时历史数据
     *
     * @param airQueryVo 请求体
     * @return 信息
     */
    @PostMapping("/airSensor/hour")
    public RestResult getAirHourHistory(@RequestBody AirQueryVo airQueryVo) {
        List<AirQueryDataModel> airHourHistory = airHourService.getAirHourHistory(airQueryVo);
        return RestResultUtil.success(airHourHistory);
    }

    /**
     * 根据设备号查询空气质量参数天历史数据
     *
     * @param airQueryVo 请求体
     * @return 信息
     */
    @PostMapping("/airSensor/day")
    public RestResult getAirDayHistory(@RequestBody AirQueryVo airQueryVo) {
        List<AirQueryDataModel> airDayHistory = airDayService.getAirDayHistory(airQueryVo);
        return RestResultUtil.success(airDayHistory);
    }
}
