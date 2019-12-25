package com.osen.aqms.modules.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.osen.aqms.common.exception.type.ServiceException;
import com.osen.aqms.common.requestVo.AccountDeviceVo;
import com.osen.aqms.modules.entity.system.Device;
import com.osen.aqms.modules.entity.system.User;
import com.osen.aqms.modules.entity.system.UserDevice;
import com.osen.aqms.modules.mapper.system.UserDeviceMapper;
import com.osen.aqms.modules.service.DeviceService;
import com.osen.aqms.modules.service.UserDeviceService;
import com.osen.aqms.modules.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

    @Autowired
    private DeviceService deviceService;

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

    @Override
    public void deleteByUids(List<Integer> uids) {
        LambdaQueryWrapper<UserDevice> wrapper = Wrappers.<UserDevice>lambdaQuery().in(UserDevice::getUserId, uids);
        super.remove(wrapper);
    }

    @Override
    public boolean updateUserToDeviceStatus(AccountDeviceVo accountDeviceVo, String type) {
        // 用户账号 & 设备账号
        String username = accountDeviceVo.getAccount();
        String deviceNo = accountDeviceVo.getDeviceNo();
        // 查询
        User byUsername = userService.findByUsername(username);
        Device deviceToNo = deviceService.findOneDeviceToNo(deviceNo);
        if (byUsername == null || deviceToNo == null)
            throw new ServiceException("绑定用户账号或设备错误，操作失败");
        LambdaQueryWrapper<UserDevice> wrapper = Wrappers.<UserDevice>lambdaQuery().eq(UserDevice::getUserId, byUsername.getId()).eq(UserDevice::getDeviceId, deviceToNo.getId());
        if (type.equals("conn")) {
            UserDevice userDevice = new UserDevice();
            userDevice.setUserId(byUsername.getId());
            userDevice.setDeviceId(deviceToNo.getId());
            // 是否重复添加
            UserDevice device = super.getOne(wrapper);
            if (device != null)
                throw new ServiceException("账号与设备已重复绑定，操作失败");
            return super.save(userDevice);
        } else {
            return super.remove(wrapper);
        }
    }

    @Override
    public List<Integer> findUserIdToDeviceId(Integer deviceId) {
        LambdaQueryWrapper<UserDevice> wrapper = Wrappers.<UserDevice>lambdaQuery().select(UserDevice::getUserId).eq(UserDevice::getDeviceId, deviceId);
        List<UserDevice> userDeviceList = super.list(wrapper);
        List<Integer> userIds = new ArrayList<>(0);
        if (userDeviceList != null && userDeviceList.size() > 0) {
            for (UserDevice userDevice : userDeviceList) {
                userIds.add(userDevice.getUserId());
            }
        }
        return userIds;
    }

    @Override
    public boolean addUserDevice(UserDevice userDevice) {
        userDevice.setCreateTime(LocalDateTime.now());
        return super.save(userDevice);
    }
}
