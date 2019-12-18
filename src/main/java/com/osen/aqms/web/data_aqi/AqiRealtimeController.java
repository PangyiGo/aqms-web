package com.osen.aqms.web.data_aqi;

import com.osen.aqms.common.model.AqiDataToMapModel;
import com.osen.aqms.common.model.AqiRealtimeMapModel;
import com.osen.aqms.common.result.RestResult;
import com.osen.aqms.common.utils.RestResultUtil;
import com.osen.aqms.common.utils.SecurityUtil;
import com.osen.aqms.modules.service.AqiRealtimeService;
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
 * Time: 17:53
 * Description:
 */
@RestController
@RequestMapping("${restful.prefix}")
@Slf4j
public class AqiRealtimeController {

    @Autowired
    private AqiRealtimeService aqiRealtimeService;

    /**
     * 根据用户名获取用户所有设备列表并封装到地图模型实体
     *
     * @return 信息
     */
    @PostMapping("/aqiRealtime/mapAqi")
    public RestResult getAllMapAqiToUsername() {
        // 用户名
        String username = SecurityUtil.getUsername();
        List<AqiRealtimeMapModel> allMapToUsername = aqiRealtimeService.getAllMapToUsername(username);
        return RestResultUtil.success(allMapToUsername);
    }

    /**
     * 根据设备号获取设备地图详细数据信息
     *
     * @param deviceNo 设备号
     * @return 信息
     */
    @PostMapping("/aqiRealtime/mapData/{deviceNo}")
    public RestResult getAqiDataMap(@PathVariable("deviceNo") String deviceNo) {
        AqiDataToMapModel aqiDataMapToDeviceNo = aqiRealtimeService.getAqiDataMapToDeviceNo(deviceNo);
        return RestResultUtil.success(aqiDataMapToDeviceNo);
    }
}
