package com.osen.aqms.web.data_aqi;

import com.osen.aqms.common.model.*;
import com.osen.aqms.common.requestVo.AirQueryVo;
import com.osen.aqms.common.requestVo.PolluteMapVo;
import com.osen.aqms.common.result.RestResult;
import com.osen.aqms.common.utils.RestResultUtil;
import com.osen.aqms.modules.service.AqiDayService;
import com.osen.aqms.modules.service.AqiHourService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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

    /**
     * 根据设备号获取当月天数的等级分布
     *
     * @param airQueryVo 请求体
     * @return 信息
     */
    @PostMapping("/aqiDay/level")
    public RestResult getLevelDayNumber(@RequestBody AirQueryVo airQueryVo) {
        LevelDayModel levelNumber = aqiDayService.getLevelNumber(airQueryVo);
        return RestResultUtil.success(levelNumber);
    }

    /**
     * 获取污染物地图数据
     *
     * @param polluteMapVo 请求体
     * @param type         hour表示小时，day表示天
     * @param sensor       参数因子
     * @return 信息
     */
    @PostMapping("/pollute/{type}/{sensor}")
    public RestResult getSensorData(@RequestBody PolluteMapVo polluteMapVo, @PathVariable("type") String type, @PathVariable("sensor") String sensor) {
        List<PolluteMapResultModel> polluteMapResultModels = new ArrayList<>(0);
        if (type.equals("hour"))
            polluteMapResultModels = aqiHourService.getSensorData(polluteMapVo, sensor);
        if (type.equals("day"))
            polluteMapResultModels = aqiDayService.getSensorData(polluteMapVo, sensor);
        return RestResultUtil.success(polluteMapResultModels);
    }

    /**
     * 获取aiq的选项数据
     *
     * @param type   时间类型 1表示24小时 2表示48小时 3表示72小时 4表示7日 5表示30日
     * @param sensor 参数 "aqi", "pm25", "pm10", "so2", "no2", "co", "o3", "voc"
     * @return 信息
     */
    @PostMapping("/aqiSensor/{deviceNo}/{type}/{sensor}")
    public RestResult getSensorData(@PathVariable("type") String type, @PathVariable("sensor") String sensor, @PathVariable("deviceNo") String deviceNo) {
        if (Integer.parseInt(type) == 1 || Integer.parseInt(type) == 2 || Integer.parseInt(type) == 3) {
            List<AqiSensorModel> aqiSensorModel = aqiHourService.getAqiSensorModel(deviceNo,  Integer.parseInt(type), sensor);
            return RestResultUtil.success(aqiSensorModel);
        } else {
            List<AqiSensorDayModel> aqiSensorHistory = aqiDayService.getAqiSensorHistory(deviceNo, type, sensor);
            return RestResultUtil.success(aqiSensorHistory);
        }
    }

    /**
     * 排名
     *
     * @param order  asc升序，desc降序
     * @param type   1表示实时，2表示昨天，3表示当月
     * @param sensor 参数 "aqi", "pm25", "pm10", "so2", "no2", "co", "o3", "voc"
     * @return 信息
     */
    @PostMapping("/sensorRank/{order}/{type}/{sensor}")
    public RestResult getSensorRank(@PathVariable("order") String order, @PathVariable("type") String type, @PathVariable("sensor") String sensor) {
        List<AqiSensorRankModel> aqiSensorRank = aqiHourService.getAqiSensorRank(order, type, sensor);
        return RestResultUtil.success(aqiSensorRank);
    }
}
