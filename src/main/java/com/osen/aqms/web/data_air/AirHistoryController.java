package com.osen.aqms.web.data_air;

import com.osen.aqms.common.model.AirDataModel;
import com.osen.aqms.common.model.AirRealTimeModel;
import com.osen.aqms.common.result.RestResult;
import com.osen.aqms.common.utils.RestResultUtil;
import com.osen.aqms.common.utils.SecurityUtil;
import com.osen.aqms.modules.entity.system.Device;
import com.osen.aqms.modules.service.AirHistoryService;
import com.osen.aqms.modules.service.DeviceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
}
