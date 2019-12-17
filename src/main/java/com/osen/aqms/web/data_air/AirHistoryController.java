package com.osen.aqms.web.data_air;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.osen.aqms.common.exception.type.ControllerException;
import com.osen.aqms.common.model.AqiRealtimeModel;
import com.osen.aqms.common.model.AirRealTimeModel;
import com.osen.aqms.common.result.RestResult;
import com.osen.aqms.common.utils.RedisOpsUtil;
import com.osen.aqms.common.utils.RestResultUtil;
import com.osen.aqms.common.utils.TableNameUtil;
import com.osen.aqms.modules.entity.system.Device;
import com.osen.aqms.modules.service.DeviceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    private RedisOpsUtil redisOpsUtil;

    @Autowired
    private DeviceService deviceService;

    /**
     * 根据设备号获取设备当前最新实时数据
     *
     * @param deviceNo 设备号
     * @return 信息
     */
    @PostMapping("/airHistory/realtime/{deviceNo}")
    public RestResult getAirRealtime(@PathVariable("deviceNo") String deviceNo) {

        AirRealTimeModel realTimeModel = new AirRealTimeModel();
        // 获取设备信息
        Device device = deviceService.findOneDeviceToNo(deviceNo);
        if (device == null)
            throw new ControllerException("无法查询设备");
        realTimeModel.setDevice(device);

        // 获取实时数据
        AqiRealtimeModel aqiRealtimeModel = new AqiRealtimeModel();
        String dataJson = redisOpsUtil.getToMap(TableNameUtil.Air_Realtime, deviceNo);
        if (!StrUtil.isEmpty(dataJson))
            aqiRealtimeModel = JSON.parseObject(dataJson, AqiRealtimeModel.class);
        realTimeModel.setAqiRealtimeModel(aqiRealtimeModel);

        // 默认获取近12小时的PM2.5参数历史曲线


        return RestResultUtil.success(realTimeModel);
    }
}
