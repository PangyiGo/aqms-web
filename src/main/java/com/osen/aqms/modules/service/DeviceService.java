package com.osen.aqms.modules.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.osen.aqms.common.model.DeviceListDataModel;
import com.osen.aqms.common.model.DeviceStatusModel;
import com.osen.aqms.common.model.DeviceTreeModel;
import com.osen.aqms.common.requestVo.UserGetVo;
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

    /**
     * 根据用户名获取对应的所有设备列表
     *
     * @param username 用户名
     * @return 信息
     */
    List<Device> findDeviceAllToUsername(String username);

    /**
     * 根据当前用户名，按地区查询设备列表
     *
     * @param address 区域名
     * @param level   区域等级(有可能是设备号)
     * @return 信息
     */
    List<Device> findDeviceGroupByAddress(String address, String level);

    /**
     * 获取当前用户的设备列表状态信息
     *
     * @param number 分页数
     * @return 信息
     */
    DeviceStatusModel findDeviceStatus(String number);

    /**
     * 分页查询当前设备列表
     *
     * @param userGetVo 请求体
     * @return 信息
     */
    DeviceListDataModel findDeviceListPageToCurrentUser(UserGetVo userGetVo);
}
