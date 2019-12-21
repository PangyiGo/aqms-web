package com.osen.aqms.modules.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.osen.aqms.common.model.AqiDataToMapModel;
import com.osen.aqms.common.model.AqiRankMapModel;
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
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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

    @Override
    public AqiRankMapModel getAqiRankToAddress(String address, String level) {
        AqiRankMapModel aqiRankMapModel = new AqiRankMapModel();
        // 获取区域设备列表
        List<Device> groupByAddress = deviceService.findDeviceGroupByAddress(address, level);
        if (groupByAddress.size() <= 0)
            return aqiRankMapModel;
        // 默认获取列表第一台设备
        Device firstDevice = groupByAddress.get(0);
        AqiDataToMapModel firstMapModel = new AqiDataToMapModel();
        firstMapModel.setDeviceName(firstDevice.getDeviceName());
        firstMapModel.setDeviceNo(firstDevice.getDeviceNo());
        String ade = (StrUtil.isNotEmpty(firstDevice.getProvince()) ? firstDevice.getProvince() : "") + (StrUtil.isNotEmpty(firstDevice.getCity()) ? firstDevice.getCity() : "") + (StrUtil.isNotEmpty(firstDevice.getArea()) ? firstDevice.getArea() : "");
        firstMapModel.setAddress(ade);
        firstMapModel.setInstallAddress((StrUtil.isNotEmpty(firstDevice.getAddress()) ? firstDevice.getAddress() : ""));
        firstMapModel.setLive(firstDevice.getLive() == ConstUtil.OPEN_STATUS ? "在线" : "离线");
        firstMapModel.setLongitude(firstDevice.getLongitude());
        firstMapModel.setLatitude(firstDevice.getLatitude());
        // 获取实时数据
        String dataJson = redisOpsUtil.getToMap(TableNameUtil.Air_Realtime, firstDevice.getDeviceNo());
        if (dataJson != null) {
            AqiRealtimeModel aqiRealtimeModel = JSON.parseObject(dataJson, AqiRealtimeModel.class);
            BeanUtil.copyProperties(aqiRealtimeModel, firstMapModel);
        }
        // 获取所有设备列表并按照AQI升序排名
        List<AqiRealtimeMapModel> aqiRealtimeMapModels = new ArrayList<>(0);
        for (Device device : groupByAddress) {
            AqiRealtimeMapModel model = new AqiRealtimeMapModel();
            model.setDeviceName(device.getDeviceName());
            model.setDeviceNo(device.getDeviceNo());
            model.setLive(device.getLive());
            model.setLongitude(device.getLongitude());
            model.setLatitude(device.getLatitude());
            // 获取AQI实时值
            String tempJson = redisOpsUtil.getToMap(TableNameUtil.Air_Realtime, device.getDeviceNo());
            if (tempJson != null) {
                AqiRealtimeModel aqiRealtimeModel = JSON.parseObject(tempJson, AqiRealtimeModel.class);
                model.setAqi(aqiRealtimeModel.getAqi());
            } else {
                model.setAqi(0);
            }
            aqiRealtimeMapModels.add(model);
        }
        // 排名
        aqiRealtimeMapModels = aqiRealtimeMapModels.stream().sorted(Comparator.comparing(AqiRealtimeMapModel::getAqi)).collect(Collectors.toList());

        aqiRankMapModel.setMapModel(firstMapModel);
        aqiRankMapModel.setMapModels(aqiRealtimeMapModels);
        return aqiRankMapModel;
    }
}
