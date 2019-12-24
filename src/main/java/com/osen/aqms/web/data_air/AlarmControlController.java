package com.osen.aqms.web.data_air;

import com.osen.aqms.common.model.AlarmControlModel;
import com.osen.aqms.common.result.RestResult;
import com.osen.aqms.common.utils.RestResultUtil;
import com.osen.aqms.modules.entity.alarm.AlarmControl;
import com.osen.aqms.modules.service.AlarmControlService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * User: PangYi
 * Date: 2019-12-24
 * Time: 13:52
 * Description:
 */
@RestController
@RequestMapping("${restful.prefix}")
@Slf4j
public class AlarmControlController {

    @Autowired
    private AlarmControlService alarmControlService;

    /**
     * 根据设备号获取设备号的预警设置值
     *
     * @param deviceNo 设备号
     * @return 信息
     */
    @PostMapping("/control/getInfo/{deviceNo}")
    public RestResult getAlarmControlInfo(@PathVariable("deviceNo") String deviceNo) {
        AlarmControlModel alarmControlInfo = alarmControlService.getAlarmControlInfo(deviceNo);
        return RestResultUtil.success(alarmControlInfo);
    }

    /**
     * 根据设备修改预警是否启动
     *
     * @param deviceNo 设备号
     * @param status   1表示启动，2表示关闭
     * @return 信息
     */
    @PostMapping("/control/update/{deviceNo}/{status}")
    public RestResult updateAlarmControlStatusToDevice(@PathVariable("deviceNo") String deviceNo, @PathVariable("status") String status) {
        boolean statusToDevice = alarmControlService.updateAlarmControlStatusToDevice(deviceNo, status);
        if (statusToDevice)
            return RestResultUtil.success("更新预警状态成功");
        return RestResultUtil.failed("更新预警状态失败");
    }

    /**
     * 根据设备号更新预警信息
     *
     * @param alarmControl 请求体
     * @return 信息
     */
    @PostMapping("/control/update/data")
    public RestResult updateAlarmControlData(@RequestBody AlarmControl alarmControl) {
        boolean b = alarmControlService.updateAlarmControlData(alarmControl);
        if (b)
            return RestResultUtil.success("更新预警值成功");
        return RestResultUtil.failed("更新预警值失败");
    }
}
