package com.osen.aqms.web.system_device;

import com.osen.aqms.common.model.DeviceTreeModel;
import com.osen.aqms.common.result.RestResult;
import com.osen.aqms.common.utils.RestResultUtil;
import com.osen.aqms.common.utils.SecurityUtil;
import com.osen.aqms.modules.entity.system.Device;
import com.osen.aqms.modules.service.DeviceService;
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
 * Time: 15:17
 * Description:
 */
@RestController
@RequestMapping("${restful.prefix}")
@Slf4j
public class SysDeviceController {

    @Autowired
    private DeviceService deviceService;

    /**
     * 根据登录用户获取设备的树形列表
     *
     * @return 信息
     */
    @PostMapping("/device/treeList")
    public RestResult getDeviceTreeToUsername() {
        // 当前登录用户名
        String username = SecurityUtil.getUsername();
        List<DeviceTreeModel> treeList = deviceService.findDeviceTreeListToUsername(username);
        // 返回
        return RestResultUtil.success(treeList);
    }

    /**
     * 根据设备号查询设备详细信息
     *
     * @return 信息
     */
    @PostMapping("/device/infoDetail/{deviceNo}")
    public RestResult getDeviceInfo(@PathVariable("deviceNo") String deviceNo) {
        Device oneDeviceToNo = deviceService.findOneDeviceToNo(deviceNo);
        if (oneDeviceToNo == null)
            return RestResultUtil.failed("无法查询设备");
        return RestResultUtil.success(oneDeviceToNo);
    }
}
