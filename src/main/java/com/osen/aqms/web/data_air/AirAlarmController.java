package com.osen.aqms.web.data_air;

import com.osen.aqms.common.model.AqiDataToMapModel;
import com.osen.aqms.common.requestVo.AddressVo;
import com.osen.aqms.common.requestVo.AirQueryVo;
import com.osen.aqms.common.result.RestResult;
import com.osen.aqms.common.utils.RestResultUtil;
import com.osen.aqms.modules.entity.alarm.AirAlarm;
import com.osen.aqms.modules.service.AirAlarmService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * User: PangYi
 * Date: 2019-12-24
 * Time: 13:52
 * Description:
 */
@RestController
@RequestMapping("${restful.prefix}")
@Slf4j
public class AirAlarmController {

    @Autowired
    private AirAlarmService airAlarmService;

    /**
     * 获取区域设备实时报警数据
     *
     * @param addressVo 请求体
     * @return 信息
     */
    @PostMapping("/airAlarm/realtime")
    public RestResult getAirAlarmRealtime(@RequestBody(required = false) AddressVo addressVo) {
        List<AqiDataToMapModel> airAlarmRealtime = airAlarmService.getAirAlarmRealtime(addressVo);
        return RestResultUtil.success(airAlarmRealtime);
    }

    /**
     * 根据设备号查询设备报警记录
     *
     * @param airQueryVo 请求体
     * @return 信息
     */
    @PostMapping("/airAlarm/history")
    public RestResult getAirAlarmHistory(@RequestBody AirQueryVo airQueryVo) {
        List<AirAlarm> airAlarmHistory = airAlarmService.getAirAlarmHistory(airQueryVo);
        return RestResultUtil.success(airAlarmHistory);
    }
}
