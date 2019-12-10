package com.osen.aqms.modules.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.osen.aqms.modules.entity.system.Role;
import com.osen.aqms.modules.entity.system.User;
import com.osen.aqms.modules.mapper.system.UserMapper;
import com.osen.aqms.modules.service.RoleService;
import com.osen.aqms.modules.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * User: PangYi
 * Date: 2019-11-30
 * Time: 10:52
 * Description:
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private RoleService roleService;

    @Override
    public User findByUsername(String username) {
        User user = null;
        try {
            // 查询多条记录，抛出异常
            LambdaQueryWrapper<User> wrapper = Wrappers.<User>lambdaQuery().select(User::getAccount, User::getId, User::getPassword, User::getStatus).eq(User::getAccount, username);
            user = super.getOne(wrapper, true);
            if (BeanUtil.isEmpty(user))
                return null;
            // 获取用户对应的角色
            List<Role> roleByUserId = roleService.findRoleByUserId(user.getId());
            if (CollectionUtil.isEmpty(roleByUserId))
                return null;
            // 保存用户角色
            user.setRoles(roleByUserId);
        } catch (Exception e) {
            return null;
        }
        return user;
    }
}
