package com.osen.aqms.modules.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.osen.aqms.modules.entity.system.User;
import com.osen.aqms.modules.entity.system.UserDevice;
import com.osen.aqms.modules.mapper.system.UserDeviceMapper;
import com.osen.aqms.modules.service.UserDeviceService;
import com.osen.aqms.modules.service.UserService;
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
public class UserDeviceServiceImpl extends ServiceImpl<UserDeviceMapper, UserDevice> implements UserDeviceService {

    @Autowired
    private UserService userService;

    @Override
    public List<Integer> findDeviceIdToUserName(String username) {
        List<Integer> deviceIds = new ArrayList<>(0);
        // 查询用户
        LambdaQueryWrapper<User> findUser = Wrappers.<User>lambdaQuery().eq(User::getAccount, username).select(User::getId);
        try {
            User user = userService.getOne(findUser, true);
            // 根据用户ID查询关联设备
            LambdaQueryWrapper<UserDevice> findDeviceId = Wrappers.<UserDevice>lambdaQuery().eq(UserDevice::getUserId, user.getId()).select(UserDevice::getDeviceId);
            List<UserDevice> userDevices = super.list(findDeviceId);
            if (userDevices != null && userDevices.size() > 0) {
                for (UserDevice userDevice : userDevices) {
                    deviceIds.add(userDevice.getDeviceId());
                }
            }
        } catch (Exception e) {
            return deviceIds;
        }
        return deviceIds;
    }
}
