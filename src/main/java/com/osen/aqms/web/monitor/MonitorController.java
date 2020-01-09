package com.osen.aqms.web.monitor;

import com.osen.aqms.common.model.DeviceNumberModel;
import com.osen.aqms.common.requestVo.AddressVo;
import com.osen.aqms.common.result.RestResult;
import com.osen.aqms.common.utils.RestResultUtil;
import com.osen.aqms.modules.entity.address.WebAddress;
import com.osen.aqms.modules.service.DeviceService;
import com.osen.aqms.modules.service.WebAddressService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
}
