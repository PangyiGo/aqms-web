package com.osen.aqms.modules.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.osen.aqms.modules.entity.system.Role;

import java.util.List;

/**
 * User: PangYi
 * Date: 2019-11-30
 * Time: 9:24
 * Description:
 */
public interface RoleService extends IService<Role> {

    /**
     * 根据用户ID查询对应角色
     *
     * @param uid 用户ID
     * @return 数据
     */
    List<Role> findRoleByUserId(Integer uid);
}
