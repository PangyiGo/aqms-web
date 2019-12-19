package com.osen.aqms.web.system_user;

import com.osen.aqms.common.result.RestResult;
import com.osen.aqms.common.utils.RestResultUtil;
import com.osen.aqms.common.utils.SecurityUtil;
import com.osen.aqms.modules.entity.system.User;
import com.osen.aqms.modules.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * User: PangYi
 * Date: 2019-12-19
 * Time: 9:01
 * Description:
 */
@RestController
@RequestMapping("${restful.prefix}")
@Slf4j
public class SysUserController {

    @Autowired
    private UserService userService;

    /**
     * 获取用户详细信息
     *
     * @return 信息
     */
    @PostMapping("/user/infoDetail")
    public RestResult getUserInfoDetail() {
        // 用户名称
        String username = SecurityUtil.getUsername();
        User user = userService.findByUsername(username);
        if (user == null)
            return RestResultUtil.failed("请求用户信息失败");
        return RestResultUtil.success(user);
    }
}
