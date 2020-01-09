package com.osen.aqms.modules.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.osen.aqms.common.utils.SecurityUtil;
import com.osen.aqms.modules.entity.address.WebAddress;
import com.osen.aqms.modules.entity.system.Device;
import com.osen.aqms.modules.mapper.address.WebAddressMapper;
import com.osen.aqms.modules.service.DeviceService;
import com.osen.aqms.modules.service.UserDeviceService;
import com.osen.aqms.modules.service.WebAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * User: PangYi
 * Date: 2019-11-30
 * Time: 10:52
 * Description:
 */
@Service
public class WebAddressServiceImpl extends ServiceImpl<WebAddressMapper, WebAddress> implements WebAddressService {

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private UserDeviceService userDeviceService;

    @Override
    public List<WebAddress> findAddressGroup() {
        List<WebAddress> webAddressList = new ArrayList<>(0);
        String username = SecurityUtil.getUsername();
        List<Integer> deviceIds = userDeviceService.findDeviceIdToUserName(username);
        if (deviceIds.size() <= 0)
            return webAddressList;
        // 分组查询省份
        LambdaQueryWrapper<Device> queryWrapper = Wrappers.<Device>lambdaQuery().in(Device::getId, deviceIds).isNotNull(Device::getProvince).groupBy(Device::getProvince).select(Device::getProvince);
        List<Device> deviceList = deviceService.list(queryWrapper);
        // 省份名称编号
        List<String> names = new ArrayList<>(0);
        deviceList.forEach(device -> names.add(device.getProvince()));
        LambdaQueryWrapper<WebAddress> wrapper = Wrappers.<WebAddress>lambdaQuery().in(WebAddress::getName, names);
        List<WebAddress> addressList = super.list(wrapper);
        if (addressList != null && addressList.size() > 0)
            webAddressList = addressList;
        return webAddressList;
    }
}
