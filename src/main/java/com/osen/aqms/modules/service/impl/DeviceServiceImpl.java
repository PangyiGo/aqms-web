package com.osen.aqms.modules.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.osen.aqms.common.model.DeviceTreeModel;
import com.osen.aqms.common.utils.RedisOpsUtil;
import com.osen.aqms.common.utils.SecurityUtil;
import com.osen.aqms.common.utils.TableNameUtil;
import com.osen.aqms.modules.entity.system.Device;
import com.osen.aqms.modules.mapper.system.DeviceMapper;
import com.osen.aqms.modules.service.DeviceService;
import com.osen.aqms.modules.service.UserDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * User: PangYi
 * Date: 2019-11-30
 * Time: 10:52
 * Description:
 */
@Service
public class DeviceServiceImpl extends ServiceImpl<DeviceMapper, Device> implements DeviceService {

    @Autowired
    private UserDeviceService userDeviceService;

    @Autowired
    private RedisOpsUtil redisOpsUtil;

    @Override
    public List<DeviceTreeModel> findDeviceTreeListToUsername(String username) {
        // 全局结果
        List<DeviceTreeModel> deviceTreeModels = new ArrayList<>(0);

        // 从缓存中查询设备节点
        boolean key = redisOpsUtil.hashKeyToMap(TableNameUtil.DeviceTree_List, username);
        if (key) {
            String dataJson = redisOpsUtil.getToMap(TableNameUtil.DeviceTree_List, username);
            deviceTreeModels = JSON.parseObject(dataJson, List.class);
        } else {
            // 查询用户关联设备ID
            List<Integer> deviceIds = userDeviceService.findDeviceIdToUserName(username);
            if (deviceIds.size() > 0) {
                // 查询设备全部列表
                List<Device> deviceList = this.findDeviceToDeviceIds(deviceIds);
                // 查询省级节点列表
                List<Device> provinceList = this.findDeviceGroupByAddress(null, null, 1, deviceIds);
                for (Device province : provinceList) {
                    // 省级节点
                    DeviceTreeModel provinceNode = new DeviceTreeModel();
                    provinceNode.setLabel(province.getProvince().trim().equals("") ? "一级地区" : province.getProvince());
                    provinceNode.setCode(province.getProvinceCode());
                    provinceNode.setDeviceNo("province");
                    List<DeviceTreeModel> provinceChild = new ArrayList<>(0);
                    // 查询市级节点列表
                    List<Device> cityList = this.findDeviceGroupByAddress(province.getProvince(), null, 2, deviceIds);
                    for (Device city : cityList) {
                        // 判断省级与市级名称是否相同
                        if (province.getProvince().trim().equals(city.getCity().trim())) {
                            // 查询区级节点列表
                            List<Device> areaList = this.findDeviceGroupByAddress(province.getProvince(), city.getCity(), 3, deviceIds);
                            for (Device area : areaList) {
                                // 判断市级与区级名称是否相同
                                if (city.getCity().trim().equals(area.getArea().trim())) {
                                    // 具体区级下的设备列表
                                    List<Device> devices = deviceList.stream() // 数据流
                                            .filter(dev -> dev.getProvince().equals(province.getProvince())) // 省级
                                            .filter(dev -> dev.getCity().equals(city.getCity())) // 市级
                                            .filter(dev -> dev.getArea().equals(area.getArea())) // 区级
                                            .collect(Collectors.toList());
                                    for (Device device : devices) {
                                        DeviceTreeModel node = new DeviceTreeModel();
                                        node.setLabel(device.getDeviceName());
                                        node.setCode(device.getId().toString());
                                        node.setDeviceNo(device.getDeviceNo());
                                        // 添加设备
                                        provinceChild.add(node);
                                    }
                                } else {
                                    DeviceTreeModel areaNode = new DeviceTreeModel();
                                    areaNode.setLabel(area.getArea().trim().equals("") ? "三级地区" : area.getArea());
                                    areaNode.setCode(area.getAreaCode());
                                    areaNode.setDeviceNo("area");
                                    List<DeviceTreeModel> areaClid = new ArrayList<>(0);
                                    // 具体区级下的设备列表
                                    List<Device> devices = deviceList.stream() // 数据流
                                            .filter(dev -> dev.getProvince().equals(province.getProvince())) // 省级
                                            .filter(dev -> dev.getCity().equals(city.getCity())) // 市级
                                            .filter(dev -> dev.getArea().equals(area.getArea())) // 区级
                                            .collect(Collectors.toList());
                                    for (Device device : devices) {
                                        DeviceTreeModel node = new DeviceTreeModel();
                                        node.setLabel(device.getDeviceName());
                                        node.setCode(device.getId().toString());
                                        node.setDeviceNo(device.getDeviceNo());
                                        // 添加设备
                                        areaClid.add(node);
                                    }
                                    // 保存区级的设备列表
                                    areaNode.setChildren(areaClid);
                                    // 提升区域等级，添加区级节点
                                    provinceChild.add(areaNode);
                                }
                            }
                        } else {
                            // 市级节点
                            DeviceTreeModel cityNode = new DeviceTreeModel();
                            cityNode.setLabel(city.getCity().trim().equals("") ? "二级地区" : city.getCity());
                            cityNode.setCode(city.getCityCode());
                            cityNode.setDeviceNo("city");
                            List<DeviceTreeModel> cityChild = new ArrayList<>(0);
                            // 查询区级节点列表
                            List<Device> areaList = this.findDeviceGroupByAddress(province.getProvince(), city.getCity(), 3, deviceIds);
                            for (Device area : areaList) {
                                // 判断市级与区级名称是否相同
                                if (city.getCity().trim().equals(area.getArea().trim())) {
                                    List<Device> devices = deviceList.stream() // 数据流
                                            .filter(dev -> dev.getProvince().equals(province.getProvince())) // 省级
                                            .filter(dev -> dev.getCity().equals(city.getCity())) // 市级
                                            .filter(dev -> dev.getArea().equals(area.getArea())) // 区级
                                            .collect(Collectors.toList());
                                    for (Device device : devices) {
                                        DeviceTreeModel node = new DeviceTreeModel();
                                        node.setLabel(device.getDeviceName());
                                        node.setCode(device.getId().toString());
                                        node.setDeviceNo(device.getDeviceNo());
                                        // 添加市级设备
                                        cityChild.add(node);
                                    }
                                } else {
                                    DeviceTreeModel areaNode = new DeviceTreeModel();
                                    areaNode.setLabel(area.getArea().trim().equals("") ? "三级地区" : area.getArea());
                                    areaNode.setCode("");
                                    areaNode.setDeviceNo("area");
                                    List<DeviceTreeModel> areaClid = new ArrayList<>(0);
                                    // 具体区级下的设备列表
                                    List<Device> devices = deviceList.stream() // 数据流
                                            .filter(dev -> dev.getProvince().equals(province.getProvince())) // 省级
                                            .filter(dev -> dev.getCity().equals(city.getCity())) // 市级
                                            .filter(dev -> dev.getArea().equals(area.getArea())) // 区级
                                            .collect(Collectors.toList());
                                    for (Device device : devices) {
                                        DeviceTreeModel node = new DeviceTreeModel();
                                        node.setLabel(device.getDeviceName());
                                        node.setCode(device.getId().toString());
                                        node.setDeviceNo(device.getDeviceNo());
                                        // 添加设备
                                        areaClid.add(node);
                                    }
                                    // 保存区级的设备列表
                                    areaNode.setChildren(areaClid);
                                    // 添加市级
                                    cityChild.add(areaNode);
                                }
                            }
                            cityNode.setChildren(cityChild);
                            // 添加市级节点
                            provinceChild.add(cityNode);
                        }
                    }
                    provinceNode.setChildren(provinceChild);
                    // 父节点
                    deviceTreeModels.add(provinceNode);
                }

                // 保存数据缓存
                redisOpsUtil.putToMap(TableNameUtil.DeviceTree_List, username, JSON.toJSONString(deviceTreeModels));
            }
        }
        return deviceTreeModels;
    }

    /**
     * 设备地区分组查询
     *
     * @param province  省级
     * @param city      市级
     * @param type      类型
     * @param deviceIds 设备ID列表
     * @return 信息
     */
    private List<Device> findDeviceGroupByAddress(String province, String city, int type, List<Integer> deviceIds) {
        LambdaQueryWrapper<Device> wrapper = Wrappers.<Device>lambdaQuery();
        switch (type) {
            case 1:
                wrapper.in(Device::getId, deviceIds).select(Device::getProvince, Device::getProvinceCode).groupBy(Device::getProvince, Device::getProvinceCode);
                break;
            case 2:
                wrapper.in(Device::getId, deviceIds).eq(Device::getProvince, province).select(Device::getCity, Device::getCityCode).groupBy(Device::getCity, Device::getCityCode);
                break;
            case 3:
                wrapper.in(Device::getId, deviceIds).eq(Device::getProvince, province).eq(Device::getCity, city).select(Device::getArea, Device::getAreaCode).groupBy(Device::getArea, Device::getAreaCode);
                break;
        }
        return super.list(wrapper);
    }

    @Override
    public List<Device> findDeviceToDeviceIds(List<Integer> deviceIds) {
        List<Device> devices = new ArrayList<>(0);
        if (deviceIds != null && deviceIds.size() > 0) {
            devices = new ArrayList<>(super.listByIds(deviceIds));
        }
        return devices;
    }

    @Override
    public Device findOneDeviceToNo(String deviceNo) {
        try {
            LambdaQueryWrapper<Device> wrapper = Wrappers.<Device>lambdaQuery().eq(Device::getDeviceNo, deviceNo);
            Device device = super.getOne(wrapper, true);
            return BeanUtil.isEmpty(device) ? null : device;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<Device> findDeviceAllToUsername(String username) {
        List<Device> devices = new ArrayList<>(0);
        // 获取当前用户的设备ID
        List<Integer> deviceIds = userDeviceService.findDeviceIdToUserName(username);
        if (deviceIds.size() > 0)
            devices = this.findDeviceToDeviceIds(deviceIds);
        return devices;
    }

    @Override
    public List<Device> findDeviceGroupByAddress(String address, String level) {
        List<Device> deviceList = new ArrayList<>(0);
        // 获取当前用户的设备ID
        String username = SecurityUtil.getUsername();
        List<Integer> deviceIds = userDeviceService.findDeviceIdToUserName(username);
        if (deviceIds.size() <= 0)
            return deviceList;
        // 区域分组查询
        LambdaQueryWrapper<Device> query = Wrappers.<Device>lambdaQuery();
        switch (level) {
            case "province":
                query.in(Device::getId, deviceIds).eq(Device::getProvince, address);
                deviceList = super.list(query);
                break;
            case "city":
                query.in(Device::getId, deviceIds).eq(Device::getCity, address);
                deviceList = super.list(query);
                break;
            case "area":
                query.in(Device::getId, deviceIds).eq(Device::getArea, address);
                deviceList = super.list(query);
                break;
            default:
                query.eq(Device::getDeviceNo, level);
                deviceList = super.list(query);
                break;
        }
        return deviceList;
    }

}
