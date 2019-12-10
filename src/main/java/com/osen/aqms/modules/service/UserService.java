package com.osen.aqms.modules.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.osen.aqms.modules.entity.system.User;

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
}
