package com.osen.aqms.modules.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.osen.aqms.common.utils.RedisOpsUtil;
import com.osen.aqms.common.utils.SecurityUtil;
import com.osen.aqms.common.utils.TableNameUtil;
import com.osen.aqms.modules.entity.message.MessageDevice;
import com.osen.aqms.modules.entity.system.Device;
import com.osen.aqms.modules.mapper.message.MessageDeviceMapper;
import com.osen.aqms.modules.service.DeviceService;
import com.osen.aqms.modules.service.MessageDeviceService;
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
public class MessageDeviceServiceImpl extends ServiceImpl<MessageDeviceMapper, MessageDevice> implements MessageDeviceService {

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private RedisOpsUtil redisOpsUtil;

    @Override
    public List<MessageDevice> getMessageToUsername(String username) {
        List<MessageDevice> messageDevices = new ArrayList<>(0);
        // 获取用户设备列表
        List<Device> deviceList = deviceService.findDeviceAllToUsername(username);
        for (Device device : deviceList) {
            String status = redisOpsUtil.getToMap(TableNameUtil.Msg_DeviceStatus, device.getDeviceNo());
            if (StrUtil.isNotEmpty(status))
                messageDevices.add(JSON.parseObject(status, MessageDevice.class));
            String alarm = redisOpsUtil.getToMap(TableNameUtil.Msg_Alarm, device.getDeviceNo());
            if (StrUtil.isNotEmpty(alarm))
                messageDevices.add(JSON.parseObject(alarm, MessageDevice.class));
            String warning = redisOpsUtil.getToMap(TableNameUtil.Msg_Warning, device.getDeviceNo());
            if (StrUtil.isNotEmpty(warning))
                messageDevices.add(JSON.parseObject(warning, MessageDevice.class));
        }
        return messageDevices;
    }

    @Override
    public void deleteMessageToTypeAndDeviceNo(String type, String deviceNo) {
        if (type.equals("status")) {
            redisOpsUtil.deleteToMap(TableNameUtil.Msg_DeviceStatus, deviceNo);
        } else if (type.equals("alarm")) {
            redisOpsUtil.deleteToMap(TableNameUtil.Msg_Alarm, deviceNo);
        } else if (type.equals("warning")) {
            redisOpsUtil.deleteToMap(TableNameUtil.Msg_Warning, deviceNo);
        } else if (type.equals("all") && deviceNo.equals("all")) {
            String username = SecurityUtil.getUsername();
            // 获取用户设备列表
            List<Device> deviceList = deviceService.findDeviceAllToUsername(username);
            for (Device device : deviceList) {
                redisOpsUtil.deleteToMap(TableNameUtil.Msg_DeviceStatus, device.getDeviceNo());
                redisOpsUtil.deleteToMap(TableNameUtil.Msg_Alarm, device.getDeviceNo());
                redisOpsUtil.deleteToMap(TableNameUtil.Msg_Warning, device.getDeviceNo());
            }
        }
    }
}
