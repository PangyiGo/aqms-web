package com.osen.aqms.modules.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.osen.aqms.common.requestVo.AccountDeviceVo;
import com.osen.aqms.modules.entity.system.UserDevice;

import java.util.List;

/**
 * User: PangYi
 * Date: 2019-11-30
 * Time: 9:24
 * Description:
 */
public interface UserDeviceService extends IService<UserDevice> {

    /**
     * 根据用户名查询用户关联设备ID
     *
     * @param username 用户名
     * @return 信息
     */
    List<Integer> findDeviceIdToUserName(String username);

    /**
     * 批量删除用户设备关联
     *
     * @param uids 用户IDs
     */
    void deleteByUids(List<Integer> uids);

    /**
     * 指定设备与指定账号关联或取消关联
     *
     * @param accountDeviceVo 请求体
     * @param type            conn 表示关联 cancel 表示取消关联
     * @return 信息
     */
    boolean updateUserToDeviceStatus(AccountDeviceVo accountDeviceVo, String type);

    /**
     * 根据用户名查询用户关联的用户ID
     *
     * @param deviceId 设备ID
     * @return 信息
     */
    List<Integer> findUserIdToDeviceId(Integer deviceId);

    /**
     * 添加用户设备关联
     *
     * @param userDevice 关联
     * @return 信息
     */
    boolean addUserDevice(UserDevice userDevice);
}
