package com.osen.aqms.web.data_air;

import com.osen.aqms.common.exception.type.ControllerException;
import com.osen.aqms.common.model.AirAccordModel;
import com.osen.aqms.common.model.AirDataModel;
import com.osen.aqms.common.model.AirRealTimeModel;
import com.osen.aqms.common.model.AqiDataToMapModel;
import com.osen.aqms.common.requestVo.AirAccordVo;
import com.osen.aqms.common.requestVo.AirRankVo;
import com.osen.aqms.common.result.RestResult;
import com.osen.aqms.common.utils.RestResultUtil;
import com.osen.aqms.common.utils.SecurityUtil;
import com.osen.aqms.modules.entity.system.Device;
import com.osen.aqms.modules.service.AirHistoryService;
import com.osen.aqms.modules.service.DeviceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * User: PangYi
 * Date: 2019-12-17
 * Time: 13:50
 * Description: 空气站实时数据控制器
 */
@RestController
@RequestMapping("${restful.prefix}")
@Slf4j
public class AirHistoryController {

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private AirHistoryService airHistoryService;

    /**
     * 根据设备号获取设备当前最新实时数据
     *
     * @param deviceNo 设备号
     * @return 信息
     */
    @PostMapping("/airHistory/realtime/{deviceNo}")
    public RestResult getAirRealtime(@PathVariable("deviceNo") String deviceNo) {
        return RestResultUtil.success(airHistoryService.getAirRealtime(deviceNo));
    }

    /**
     * 加载首页获取默认第一台设备实时数据
     *
     * @return 信息
     */
    @PostMapping("/airHistory/firstDevice")
    public RestResult getFirstRealtime() {
        AirRealTimeModel airRealTimeModel = new AirRealTimeModel();
        // 用户名
        String username = SecurityUtil.getUsername();
        // 获取用户设备列表
        List<Device> allToUsername = deviceService.findDeviceAllToUsername(username);
        if (allToUsername.size() > 0) {
            Device device = allToUsername.get(0);
            airRealTimeModel = airHistoryService.getAirRealtime(device.getDeviceNo());
        }
        return RestResultUtil.success(airRealTimeModel);
    }

    /**
     * 根据设备号获取指定监控参数数据
     *
     * @param deviceNo 设备号
     * @param sensor   监控因子
     * @return 信息
     */
    @PostMapping("/airHistory/sensor/{deviceNo}/{sensor}")
    public RestResult getDataToSensor(@PathVariable("deviceNo") String deviceNo, @PathVariable("sensor") String sensor) {
        List<AirDataModel> airDataToSensor = airHistoryService.getAirDataToSensor(deviceNo, sensor);
        return RestResultUtil.success(airDataToSensor);
    }

    /**
     * 查询当前用户的实时数据列表
     *
     * @param params 无参数默认查询全部，否则根据区域名称与区域等级查询
     * @return 信息
     */
    @PostMapping("/airHistory/realtimeList")
    public RestResult getAirRealtimeList(@RequestBody(required = false) Map<String, Object> params) {
        List<AqiDataToMapModel> airRealtimeList = airHistoryService.getAirRealtimeList(params);
        return RestResultUtil.success(airRealtimeList);
    }

    /**
     * 获取当前用户设备实时数据监测排行
     *
     * @return 信息
     */
    @PostMapping("/airHistory/rank/realtime")
    public RestResult getAirRankToRealtime(@RequestBody(required = false) AirRankVo airRankVo) {
        List<AqiDataToMapModel> airRankToRealtime = airHistoryService.getAirRankToRealtime(airRankVo);
        return RestResultUtil.success(airRankToRealtime);
    }

    /**
     * 获取当前用户设备历史数据监测排行
     * 默认查询某一天的小时平均值排行
     *
     * @return 信息
     */
    @PostMapping("/airHistory/rank/history")
    public RestResult getAirRankToHistory(@RequestBody AirRankVo airRankVo) {
        if (airRankVo.getTime() == null || "".equals(airRankVo.getTime().trim()))
            throw new ControllerException("请求体参数异常");
        List<AqiDataToMapModel> airRankToHistory = airHistoryService.getAirRankToHistory(airRankVo);
        return RestResultUtil.success(airRankToHistory);
    }

    /**
     * 获取空气站参数数据统计分析报表
     *
     * @param airAccordVo 请求体
     * @return 信息
     */
    @PostMapping("/airHistory/according")
    public RestResult getAirAccord(@RequestBody AirAccordVo airAccordVo) {
        List<AirAccordModel> airAccordToDay;
        switch (airAccordVo.getType()) {
            case "day":
                airAccordToDay = airHistoryService.getAirAccordToDay(airAccordVo);
                break;
            case "month":
                airAccordToDay = airHistoryService.getAirAccordToMonth(airAccordVo);
                break;
            default:
                throw new ControllerException("请求类型参数异常");
        }
        return RestResultUtil.success(airAccordToDay);
    }
}
