package com.osen.aqms.modules.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.osen.aqms.common.exception.type.ServiceException;
import com.osen.aqms.common.model.UserListDataModel;
import com.osen.aqms.common.requestVo.UserAccountVo;
import com.osen.aqms.common.requestVo.UserGetVo;
import com.osen.aqms.common.requestVo.UserModifyVo;
import com.osen.aqms.common.utils.ConstUtil;
import com.osen.aqms.common.utils.SecurityUtil;
import com.osen.aqms.modules.entity.system.Role;
import com.osen.aqms.modules.entity.system.User;
import com.osen.aqms.modules.entity.system.UserRole;
import com.osen.aqms.modules.mapper.system.UserMapper;
import com.osen.aqms.modules.service.RoleService;
import com.osen.aqms.modules.service.UserDeviceService;
import com.osen.aqms.modules.service.UserRoleService;
import com.osen.aqms.modules.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private UserDeviceService userDeviceService;

    @Override
    public User findByUsername(String username) {
        User user = null;
        try {
            // 查询多条记录，抛出异常
            LambdaQueryWrapper<User> wrapper = Wrappers.<User>lambdaQuery().eq(User::getAccount, username);
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

    @Override
    public UserListDataModel findUserAllListToAccount(UserGetVo userGetVo) {
        UserListDataModel userListDataModel = new UserListDataModel();
        // 当前登录用户
        String username = SecurityUtil.getUsername();
        User user = this.findByUsername(username);
        // 根据PID判断角色是否为管理员，pid等于0是为管理员
        LambdaQueryWrapper<User> wrapper = Wrappers.<User>lambdaQuery(); // 查询
        int userPid = user.getParentId();
        if (userPid != 0) {
            // 非系统管理员
            wrapper.eq(User::getParentId, userPid).ne(User::getId, user.getId());
        } else {
            // 系统管理员
            wrapper.ne(User::getId, user.getId());
        }
        String search = userGetVo.getSearch();
        if (search != null && !"".equals(search.trim()))
            wrapper.and(query -> query.like(User::getAccount, userGetVo).or().like(User::getCustomer, search).or().like(User::getCompany, search).or().like(User::getPhone, search).or().like(User::getAddress, search).or().like(User::getRemark, search));
        // 查询条件
        Page<User> userPage = new Page<>(Integer.valueOf(userGetVo.getNumber()), ConstUtil.PAGENUMBER);
        IPage<User> userIPage = super.page(userPage, wrapper);
        if (userIPage.getTotal() > 0) {
            userListDataModel.setTotal(userIPage.getTotal());
            List<User> users = new ArrayList<>(0);
            for (User record : userIPage.getRecords()) {
                List<Role> roles = roleService.findRoleByUserId(record.getId());
                record.setRoles(roles);
                users.add(record);
            }
            userListDataModel.setUserList(users);
        }
        return userListDataModel;
    }

    @Override
    public boolean userAdd(UserModifyVo userModifyVo) {
        User tempUser = this.findByUsername(userModifyVo.getAccount());
        if (!BeanUtil.isEmpty(tempUser))
            throw new ServiceException("账号已重复添加");
        if (userModifyVo.getAccount() == null || "".equals(userModifyVo.getAccount().trim()))
            throw new ServiceException("新增账号不允许为空");
        // 查询角色
        Role role = roleService.findRoleByName(userModifyVo.getRoleName() == null ? "" : userModifyVo.getRoleName());
        if (BeanUtil.isEmpty(role))
            throw new ServiceException("角色名称异常");
        // 用户信息
        User user = new User();
        user.setAccount(userModifyVo.getAccount());
        user.setPassword(ConstUtil.INIT_PASSWORD);
        user.setCustomer(userModifyVo.getCustomer());
        user.setPhone(userModifyVo.getPhone());
        user.setCompany(userModifyVo.getCompany());
        user.setAddress(userModifyVo.getAddress());
        user.setRemark(userModifyVo.getRemark());
        user.setStatus(ConstUtil.OPEN_STATUS);
        user.setParentId(SecurityUtil.getUserId()); // 当前登录的ID
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        // 添加
        boolean save = super.save(user);
        if (save) {
            // 添加角色关联
            UserRole userRole = new UserRole();
            userRole.setUserId(user.getId());
            userRole.setRoleId(role.getId());
            userRole.setCreateTime(LocalDateTime.now());
            return userRoleService.addUserRole(userRole);
        }
        return false;
    }

    @Override
    public boolean userInfoUpdate(UserModifyVo userModifyVo) {
        User user = new User();
        // 属性复制
        BeanUtil.copyProperties(userModifyVo, user);
        user.setUpdateTime(LocalDateTime.now());
        LambdaUpdateWrapper<User> wrapper = Wrappers.<User>lambdaUpdate().eq(User::getAccount, user.getAccount());
        return super.update(user, wrapper);
    }

    @Override
    public boolean userDeleteByAccount(UserAccountVo userAccountVo) {
        // 查询删除的用户
        LambdaQueryWrapper<User> wrapper = Wrappers.<User>lambdaQuery().select(User::getId).in(User::getAccount, userAccountVo.getAccounts());
        List<User> userList = super.list(wrapper);
        List<Integer> uids = new ArrayList<>(0);
        if (userList.size() <= 0)
            return false;
        for (User user : userList) {
            uids.add(user.getId());
        }
        // 删除
        userDeviceService.deleteByUids(uids);
        return super.removeByIds(uids) && userRoleService.deleteBatchByUid(uids);
    }

    @Override
    public boolean userPasswordResetByAccount(UserAccountVo userAccountVo) {
        LambdaUpdateWrapper<User> wrapper = Wrappers.<User>lambdaUpdate().set(User::getPassword, ConstUtil.INIT_PASSWORD).in(User::getAccount, userAccountVo.getAccounts());
        return super.update(wrapper);
    }

    @Override
    public List<User> findUserToUserIds(List<Integer> uids) {
        if (uids == null || uids.size() <= 0)
            return new ArrayList<>(0);
        return new ArrayList<>(super.listByIds(uids));
    }
}
