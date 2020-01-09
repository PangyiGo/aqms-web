package com.osen.aqms.web.monitor;

import com.osen.aqms.common.result.RestResult;
import com.osen.aqms.common.utils.RestResultUtil;
import com.osen.aqms.modules.entity.address.WebAddress;
import com.osen.aqms.modules.service.WebAddressService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
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
}
