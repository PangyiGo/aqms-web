package com.osen.aqms.modules.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.osen.aqms.common.model.AqiDataToMapModel;
import com.osen.aqms.common.model.AqiRealtimeMapModel;
import com.osen.aqms.common.model.AqiRealtimeModel;
import com.osen.aqms.common.utils.ConstUtil;
import com.osen.aqms.common.utils.RedisOpsUtil;
import com.osen.aqms.common.utils.TableNameUtil;
import com.osen.aqms.modules.entity.data.AqiRealtime;
import com.osen.aqms.modules.entity.system.Device;
import com.osen.aqms.modules.mapper.data.AqiRealtimeMapper;
import com.osen.aqms.modules.service.AqiRealtimeService;
import com.osen.aqms.modules.service.DeviceService;
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
public class AqiRealtimeServiceImpl extends ServiceImpl<AqiRealtimeMapper, AqiRealtime> implements AqiRealtimeService {

    @Autowired
    private RedisOpsUtil redisOpsUtil;

    @Autowired
    private DeviceService deviceService;

    @Override
    public List<AqiRealtimeMapModel> getAllMapToUsername(String username) {
        List<AqiRealtimeMapModel> aqiRealtimeMapModels = new ArrayList<>(0);
        // 用户设备列表
        List<Device> deviceList = deviceService.findDeviceAllToUsername(username);
        for (Device device : deviceList) {
            AqiRealtimeMapModel mapModel = new AqiRealtimeMapModel();
            mapModel.setDeviceName(device.getDeviceName());
            mapModel.setDeviceNo(device.getDeviceNo());
            mapModel.setLongitude(device.getLongitude());
            mapModel.setLatitude(device.getLatitude());
            mapModel.setLive(device.getLive());
            // 获取AQI实时值
            String dataJson = redisOpsUtil.getToMap(TableNameUtil.Air_Realtime, device.getDeviceNo());
            if (StrUtil.isNotEmpty(dataJson)) {
                AqiRealtimeModel aqiRealtimeModel = JSON.parseObject(dataJson, AqiRealtimeModel.class);
                mapModel.setAqi(aqiRealtimeModel.getAqi());
            } else {
                mapModel.setAqi(0);
            }
            aqiRealtimeMapModels.add(mapModel);
        }
        return aqiRealtimeMapModels;
    }

    @Override
    public AqiDataToMapModel getAqiDataMapToDeviceNo(String deviceNo) {
        AqiDataToMapModel aqiDataToMapModel = new AqiDataToMapModel();
        Device device = deviceService.findOneDeviceToNo(deviceNo);
        if (device == null)
            return aqiDataToMapModel;
        // 数据封装
        aqiDataToMapModel.setDeviceName(device.getDeviceName());
        aqiDataToMapModel.setDeviceNo(device.getDeviceNo());
        String address = (StrUtil.isNotEmpty(device.getProvince()) ? device.getProvince() : "") + (StrUtil.isNotEmpty(device.getCity()) ? device.getCity() : "") + (StrUtil.isNotEmpty(device.getArea()) ? device.getArea() : "");
        aqiDataToMapModel.setAddress(address);
        aqiDataToMapModel.setInstallAddress((StrUtil.isNotEmpty(device.getAddress()) ? device.getAddress() : ""));
        aqiDataToMapModel.setLive(device.getLive() == ConstUtil.OPEN_STATUS ? "在线" : "离线");
        aqiDataToMapModel.setLongitude(device.getLongitude());
        aqiDataToMapModel.setLatitude(device.getLatitude());
        // 获取实时数据
        String dataJson = redisOpsUtil.getToMap(TableNameUtil.Air_Realtime, deviceNo);
        if (dataJson != null) {
            AqiRealtimeModel aqiRealtimeModel = JSON.parseObject(dataJson, AqiRealtimeModel.class);
            BeanUtil.copyProperties(aqiRealtimeModel, aqiDataToMapModel);
        }
        return aqiDataToMapModel;
    }
}
