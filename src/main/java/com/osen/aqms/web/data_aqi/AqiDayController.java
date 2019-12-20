package com.osen.aqms.web.data_aqi;

import com.osen.aqms.common.model.AqiReportToDayModel;
import com.osen.aqms.common.requestVo.AqiReportVo;
import com.osen.aqms.common.result.RestResult;
import com.osen.aqms.common.utils.RestResultUtil;
import com.osen.aqms.modules.service.AqiDayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
public class AqiDayController {

    @Autowired
    private AqiDayService aqiDayService;

    /**
     * 获取AQI日报表
     *
     * @param aqiReportVo 请求体
     * @return 信息
     */
    @PostMapping("/aqiDay/dayReport")
    public RestResult getAqiReportToDay(@RequestBody AqiReportVo aqiReportVo) {
        List<AqiReportToDayModel> aqiReportToHour = aqiDayService.getAqiReportToDay(aqiReportVo);
        return RestResultUtil.success(aqiReportToHour);
    }
}
