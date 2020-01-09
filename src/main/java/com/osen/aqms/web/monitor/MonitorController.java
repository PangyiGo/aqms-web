package com.osen.aqms.web.monitor;

import com.osen.aqms.common.model.AirSensorMaxModel;
import com.osen.aqms.common.model.AqiRankModel;
import com.osen.aqms.common.model.DeviceNumberModel;
import com.osen.aqms.common.requestVo.AddressVo;
import com.osen.aqms.common.result.RestResult;
import com.osen.aqms.common.utils.RestResultUtil;
import com.osen.aqms.modules.entity.address.WebAddress;
import com.osen.aqms.modules.service.AirHistoryService;
import com.osen.aqms.modules.service.AqiHourService;
import com.osen.aqms.modules.service.DeviceService;
import com.osen.aqms.modules.service.WebAddressService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * User: PangYi
 * Date: 2020-01-09
 * Time: 10:18
 * Description:
 */
@RestController
@RequestMapping("${restful.prefix}")
@Slf4j
public class MonitorController {

    @Autowired
    private WebAddressService webAddressService;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private AqiHourService aqiHourService;

    @Autowired
    private AirHistoryService airHistoryService;

    /**
     * 获取当前用户的分组省份
     *
     * @return 信息
     */
    @PostMapping("/monitor/addressGroup")
    public RestResult getAddressGroup() {
        List<WebAddress> addressGroup = webAddressService.findAddressGroup();
        return RestResultUtil.success(addressGroup);
    }

    /**
     * 获取当前用户的设备总计数据量
     *
     * @param addressVo 请求体
     * @return 信息
     */
    @PostMapping("/monitor/deviceNumber")
    public RestResult getDeviceNumber(@RequestBody AddressVo addressVo) {
        DeviceNumberModel deviceNumberModel = deviceService.findDeviceNumber(addressVo);
        return RestResultUtil.success(deviceNumberModel);
    }

    /**
     * 根据省份分组获取设备当月的aqi排名平均值统计
     *
     * @param addressVo 请求体
     * @return 信息
     */
    @PostMapping("/monitor/rankMoth")
    public RestResult getAqiRankToMoth(@RequestBody AddressVo addressVo) {
        List<AqiRankModel> aqiRankModelToMoth = aqiHourService.getAqiRankModelToMoth(addressVo);
        return RestResultUtil.success(aqiRankModelToMoth);
    }

    /**
     * 根据省份分组获取当月AQI最优平均值设备
     *
     * @param addressVo 请求体
     * @return 信息
     */
    @PostMapping("/monitor/aqiPerfect")
    public RestResult getAqiPerfectToMonth(@RequestBody AddressVo addressVo) {
        List<AqiRankModel> aqiRankModelToMoth = aqiHourService.getAqiRankModelToMoth(addressVo);
        if (aqiRankModelToMoth.size() <= 0)
            return RestResultUtil.success(new AqiRankModel());
        // 除0
        aqiRankModelToMoth = aqiRankModelToMoth.stream().filter(aqiRankModel -> aqiRankModel.getApi() != 0).sorted(Comparator.comparing(AqiRankModel::getApi)).collect(Collectors.toList());
        return RestResultUtil.success(aqiRankModelToMoth.get(0));
    }

    /**
     * 根据省份分组获取实时数据的各个参数最大值
     *
     * @param addressVo 请求体
     * @return 信息
     */
    @PostMapping("/monitor/sensorMax")
    public RestResult getSensorMax(@RequestBody AddressVo addressVo){
        AirSensorMaxModel airSensorMax = airHistoryService.getAirSensorMax(addressVo);
        return RestResultUtil.success(airSensorMax);
    }
}
