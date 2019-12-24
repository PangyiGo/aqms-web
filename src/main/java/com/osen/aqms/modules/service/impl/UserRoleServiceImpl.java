package com.osen.aqms.modules.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.osen.aqms.modules.entity.system.UserRole;
import com.osen.aqms.modules.mapper.system.UserRoleMapper;
import com.osen.aqms.modules.service.UserRoleService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * User: PangYi
 * Date: 2019-11-30
 * Time: 10:52
 * Description:
 */
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {

    @Override
    public List<UserRole> findByUserIdToRole(Integer uid) {
        // 查询条件
        LambdaQueryWrapper<UserRole> queryWrapper = Wrappers.<UserRole>lambdaQuery().eq(UserRole::getUserId, uid);
        return super.list(queryWrapper);
    }

    @Override
    public boolean addUserRole(UserRole userRole) {
        return super.save(userRole);
    }

    @Override
    public boolean deleteBatchByUid(List<Integer> uids) {
        LambdaQueryWrapper<UserRole> wrapper = Wrappers.<UserRole>lambdaQuery().in(UserRole::getUserId, uids);
        return super.remove(wrapper);
    }
}
