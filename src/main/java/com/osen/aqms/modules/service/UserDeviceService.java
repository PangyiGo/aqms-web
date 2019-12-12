package com.osen.aqms.modules.service;

import com.baomidou.mybatisplus.extension.service.IService;
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
}
