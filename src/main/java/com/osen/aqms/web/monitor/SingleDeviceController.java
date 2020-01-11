package com.osen.aqms.web.monitor;

import com.osen.aqms.common.model.AqiViewModel;
import com.osen.aqms.common.result.RestResult;
import com.osen.aqms.common.utils.RestResultUtil;
import com.osen.aqms.modules.entity.data.AqiRealtime;
import com.osen.aqms.modules.service.AirAlarmService;
import com.osen.aqms.modules.service.AqiDayService;
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
 * Date: 2020-01-11
 * Time: 8:42
 * Description:
 */
@RestController
@RequestMapping("${restful.prefix}")
@Slf4j
public class SingleDeviceController {

    @Autowired
    private AirAlarmService airAlarmService;

    @Autowired
    private AqiDayService aqiDayService;

    @Autowired
    private AqiRealtimeService aqiRealtimeService;

    /**
     * web端大屏统计数据
     *
     * @param deviceNo 设备号
     * @return 信息
     */
    @PostMapping("/view/{deviceNo}")
    public RestResult getAlarmNumberToMonth(@PathVariable("deviceNo") String deviceNo) {
        // 报警值
        AqiViewModel alarmNumber = airAlarmService.getAlarmNumber(deviceNo);
        // 最优天数
        AqiViewModel aqiNumber = aqiDayService.getAqiNumber(deviceNo);

        AqiViewModel aqiViewModel = new AqiViewModel();
        aqiViewModel.setAlarmNumber(alarmNumber.getAlarmNumber());
        aqiViewModel.setAlarmDate(alarmNumber.getAlarmDate());
        aqiViewModel.setAlarmSensor(alarmNumber.getAlarmSensor());
        aqiViewModel.setTopSensor(alarmNumber.getTopSensor());
        aqiViewModel.setPerfect(aqiNumber.getPerfect());
        aqiViewModel.setBadDay(aqiNumber.getBadDay());

        return RestResultUtil.success(aqiViewModel);
    }

    /**
     * web端大屏统计数据
     *
     * @param deviceNo 设备号
     * @return 信息
     */
    @PostMapping("/view/aqi/{deviceNo}")
    public RestResult getOneHourToDevice(@PathVariable("deviceNo") String deviceNo) {
        List<AqiRealtime> aqiRealtime = aqiRealtimeService.getAqiRealtime(deviceNo);
        return RestResultUtil.success(aqiRealtime);
    }

}
