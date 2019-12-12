package com.osen.aqms.web;

import com.osen.aqms.common.result.RestResult;
import com.osen.aqms.common.utils.RestResultUtil;
import com.osen.aqms.modules.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * User: PangYi
 * Date: 2019-12-09
 * Time: 16:17
 * Description:
 */
@RestController
public class TestController {

    @Autowired
    private DeviceService deviceService;

    @GetMapping("/test")
    public RestResult test() {

        return RestResultUtil.success(deviceService.findDeviceTreeListToUsername("OSEN_2019"));
    }
}
