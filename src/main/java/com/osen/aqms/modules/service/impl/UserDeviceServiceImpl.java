package com.osen.aqms.modules.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.osen.aqms.modules.entity.system.UserDevice;
import com.osen.aqms.modules.mapper.system.UserDeviceMapper;
import com.osen.aqms.modules.service.UserDeviceService;
import org.springframework.stereotype.Service;

/**
 * User: PangYi
 * Date: 2019-11-30
 * Time: 10:52
 * Description:
 */
@Service
public class UserDeviceServiceImpl extends ServiceImpl<UserDeviceMapper, UserDevice> implements UserDeviceService {
}
