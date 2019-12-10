package com.osen.aqms.modules.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.osen.aqms.modules.entity.address.WebAddress;
import com.osen.aqms.modules.mapper.address.WebAddressMapper;
import com.osen.aqms.modules.service.WebAddressService;
import org.springframework.stereotype.Service;

/**
 * User: PangYi
 * Date: 2019-11-30
 * Time: 10:52
 * Description:
 */
@Service
public class WebAddressServiceImpl extends ServiceImpl<WebAddressMapper, WebAddress> implements WebAddressService {
}
