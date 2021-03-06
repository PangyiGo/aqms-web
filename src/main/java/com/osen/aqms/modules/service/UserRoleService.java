package com.osen.aqms.modules.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.osen.aqms.modules.entity.system.UserRole;

import java.util.List;

/**
 * User: PangYi
 * Date: 2019-11-30
 * Time: 9:24
 * Description:
 */
public interface UserRoleService extends IService<UserRole> {

    /**
     * 根据用户ID查询关联角色
     *
     * @param uid 用户ID
     * @return 关联数据
     */
    List<UserRole> findByUserIdToRole(Integer uid);

    /**
     * 添加用户角色关联
     *
     * @param userRole 角色
     * @return 信息
     */
    boolean addUserRole(UserRole userRole);

    /**
     * 批量删除用户角色关联数据
     *
     * @param uids 批量数据
     * @return 信息
     */
    boolean deleteBatchByUid(List<Integer> uids);
}
