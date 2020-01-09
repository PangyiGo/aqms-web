package com.osen.aqms.modules.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.osen.aqms.modules.entity.address.WebAddress;

import java.util.List;

/**
 * User: PangYi
 * Date: 2019-11-30
 * Time: 10:47
 * Description:
 */
public interface WebAddressService extends IService<WebAddress> {

    /**
     * 查询当前用户的设备省份分组
     *
     * @return 信息
     */
    List<WebAddress> findAddressGroup();
}
