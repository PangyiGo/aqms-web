package com.osen.aqms.modules.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.osen.aqms.modules.entity.system.Role;
import com.osen.aqms.modules.entity.system.UserRole;
import com.osen.aqms.modules.mapper.system.RoleMapper;
import com.osen.aqms.modules.service.RoleService;
import com.osen.aqms.modules.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * User: PangYi
 * Date: 2019-11-30
 * Time: 10:52
 * Description:
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Autowired
    private UserRoleService userRoleService;

    @Override
    public List<Role> findRoleByUserId(Integer uid) {
        List<UserRole> byUserIdToRole = userRoleService.findByUserIdToRole(uid);
        if (CollectionUtil.isEmpty(byUserIdToRole)) {
            // 为空返回
            return new ArrayList<>(0);
        }
        // 对应角色ID
        List<Integer> roleIds = new ArrayList<>();
        for (UserRole userRole : byUserIdToRole) {
            roleIds.add(userRole.getRoleId());
        }
        return CollectionUtil.list(false, super.listByIds(roleIds));
    }
}
