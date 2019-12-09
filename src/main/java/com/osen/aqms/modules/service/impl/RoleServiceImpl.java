package com.osen.aqms.modules.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.osen.aqms.modules.entity.system.Role;
import com.osen.aqms.modules.mapper.RoleMapper;
import com.osen.aqms.modules.service.RoleService;
import org.springframework.stereotype.Service;

/**
 * User: PangYi
 * Date: 2019-11-30
 * Time: 10:52
 * Description:
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {
}
