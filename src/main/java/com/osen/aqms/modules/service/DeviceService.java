package com.osen.aqms.modules.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.osen.aqms.common.model.DeviceTreeModel;
import com.osen.aqms.modules.entity.system.Device;

import java.util.List;

/**
 * User: PangYi
 * Date: 2019-11-30
 * Time: 9:24
 * Description:
 */
public interface DeviceService extends IService<Device> {

    /**
     * 根据用户名查询用户设备树形列表
     *
     * @param username 用户名
     * @return 信息
     */
    List<DeviceTreeModel> findDeviceTreeListToUsername(String username);

    /**
     * 根据设备ID批量查询设备列表
     *
     * @param deviceIds 设备ID列表
     * @return 信息
     */
    List<Device> findDeviceToDeviceIds(List<Integer> deviceIds);

    /**
     * 根据设备号查询对应设备信息
     *
     * @param deviceNo 设备号
     * @return 信息
     */
    Device findOneDeviceToNo(String deviceNo);
}
