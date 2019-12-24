package com.osen.aqms.web.data_aqi;

import com.osen.aqms.common.model.AqiDataFeatureModel;
import com.osen.aqms.common.model.AqiDataModel;
import com.osen.aqms.common.model.AqiReportToHourModel;
import com.osen.aqms.common.requestVo.AqiReportVo;
import com.osen.aqms.common.requestVo.FeatureVo;
import com.osen.aqms.common.result.RestResult;
import com.osen.aqms.common.utils.RestResultUtil;
import com.osen.aqms.modules.service.AqiHourService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
public class AqiHourController {

    @Autowired
    private AqiHourService aqiHourService;

    /**
     * 根据设备号获取近24小时历史AQI值
     *
     * @param deviceNo 设备号
     * @return 信息
     */
    @PostMapping("/aqiHour/history24h/{deviceNo}")
    public RestResult getAqiHistory24HToDeviceNo(@PathVariable("deviceNo") String deviceNo) {
        List<AqiDataModel> aqi24HToDeviceNo = aqiHourService.getAqi24HToDeviceNo(deviceNo);
        return RestResultUtil.success(aqi24HToDeviceNo);
    }

    /**
     * 获取AQI小时报表
     *
     * @param aqiReportVo 请求体
     * @return 信息
     */
    @PostMapping("/aqiHour/realtimeReport")
    public RestResult getAqiReportToHour(@RequestBody AqiReportVo aqiReportVo) {
        List<AqiReportToHourModel> aqiReportToHour = aqiHourService.getAqiReportToHour(aqiReportVo);
        return RestResultUtil.success(aqiReportToHour);
    }

    /**
     * 计算同比，环比的数据分析
     *
     * @param featureVo 请求体
     * @return 信息
     */
    @PostMapping("/aqiHour/feature")
    public RestResult getAqiFeatureData(@RequestBody FeatureVo featureVo) {
        AqiDataFeatureModel aqiFeatureData = aqiHourService.getAqiFeatureData(featureVo);
        return RestResultUtil.success(aqiFeatureData);
    }
}
