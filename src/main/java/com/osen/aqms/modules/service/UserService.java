package com.osen.aqms.modules.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.osen.aqms.common.model.UserListDataModel;
import com.osen.aqms.common.requestVo.UserAccountVo;
import com.osen.aqms.common.requestVo.UserGetVo;
import com.osen.aqms.common.requestVo.UserModifyVo;
import com.osen.aqms.modules.entity.system.User;

import java.util.List;

/**
 * User: PangYi
 * Date: 2019-11-30
 * Time: 9:24
 * Description:
 */
public interface UserService extends IService<User> {

    /**
     * 根据用户名查找用户
     *
     * @param username 用户名
     * @return 指定用户
     */
    User findByUsername(String username);

    /**
     * 获取当前用户账号下的子用户
     *
     * @param userGetVo 请求体
     * @return 信息
     */
    UserListDataModel findUserAllListToAccount(UserGetVo userGetVo);

    /**
     * 系统用户添加
     *
     * @param userModifyVo 请求体
     * @return 信息
     */
    boolean userAdd(UserModifyVo userModifyVo);

    /**
     * 系统用户信息修改
     *
     * @param userModifyVo 请求体
     * @return 信息
     */
    boolean userInfoUpdate(UserModifyVo userModifyVo);

    /**
     * 系统用户删除
     *
     * @param userAccountVo 请求体
     * @return 信息
     */
    boolean userDeleteByAccount(UserAccountVo userAccountVo);

    /**
     * 系统用户删除
     *
     * @param userAccountVo 请求体
     * @return 信息
     */
    boolean userPasswordResetByAccount(UserAccountVo userAccountVo);

    /**
     * 根据设备ID批量获取设备信息列表
     *
     * @param uids 用户Ids
     * @return 信息
     */
    List<User> findUserToUserIds(List<Integer> uids);
}
