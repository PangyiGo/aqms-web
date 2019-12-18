package com.osen.aqms.modules.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.osen.aqms.modules.entity.message.MessageDevice;

import java.util.List;

/**
 * User: PangYi
 * Date: 2019-11-30
 * Time: 9:24
 * Description:
 */
public interface MessageDeviceService extends IService<MessageDevice> {

    /**
     * 根据用户名称获取该用户设备列表的设备信息
     *
     * @param username 用户名
     * @return 信息
     */
    List<MessageDevice> getMessageToUsername(String username);

    /**
     * 根据设备类型和设备号清除设备缓存
     *
     * @param type     类型
     * @param deviceNo 设备号
     */
    void deleteMessageToTypeAndDeviceNo(String type, String deviceNo);
}
