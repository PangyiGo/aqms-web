package com.osen.aqms.web.system_user;

import com.osen.aqms.common.model.UserListDataModel;
import com.osen.aqms.common.requestVo.UserAccountVo;
import com.osen.aqms.common.requestVo.UserGetVo;
import com.osen.aqms.common.requestVo.UserModifyVo;
import com.osen.aqms.common.result.RestResult;
import com.osen.aqms.common.utils.RestResultUtil;
import com.osen.aqms.common.utils.SecurityUtil;
import com.osen.aqms.modules.entity.system.User;
import com.osen.aqms.modules.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    /**
     * 获取当前用户账号下的子用户
     *
     * @param userGetVo 请求体
     * @return 信息
     */
    @PostMapping("/user/list")
    public RestResult getUserListToAccount(@RequestBody UserGetVo userGetVo) {
        UserListDataModel userAllListToAccount = userService.findUserAllListToAccount(userGetVo);
        return RestResultUtil.success(userAllListToAccount);
    }

    /**
     * 系统用户添加
     *
     * @param userModifyVo 请求体
     * @return 信息
     */
    @PostMapping("/user/create")
    public RestResult userAdd(@RequestBody UserModifyVo userModifyVo) {
        boolean userAdd = userService.userAdd(userModifyVo);
        if (userAdd)
            return RestResultUtil.success("新账号添加成功");
        return RestResultUtil.failed("账号添加失败");
    }

    /**
     * 系统用户信息修改
     *
     * @param userModifyVo 请求体
     * @return 信息
     */
    @PostMapping("/user/infoUpdate")
    public RestResult userInfoUpdate(@RequestBody UserModifyVo userModifyVo) {
        boolean update = userService.userInfoUpdate(userModifyVo);
        if (update)
            return RestResultUtil.success("用户信息修改成功");
        return RestResultUtil.failed("用户信息修改失败");
    }

    /**
     * 系统用户删除
     *
     * @param userAccountVo 请求体
     * @return 信息
     */
    @PostMapping("/user/delete")
    public RestResult userDeleteByAccount(@RequestBody UserAccountVo userAccountVo) {
        boolean account = userService.userDeleteByAccount(userAccountVo);
        if (account)
            return RestResultUtil.success("已选用户删除成功");
        return RestResultUtil.failed("已选用户删除失败");
    }

    /**
     * 系统用户密码重置
     *
     * @param userAccountVo 请求体
     * @return 信息
     */
    @PostMapping("/user/passwordReset")
    public RestResult userPasswordResetByAccount(@RequestBody UserAccountVo userAccountVo) {
        boolean account = userService.userPasswordResetByAccount(userAccountVo);
        if (account)
            return RestResultUtil.success("密码重置成功");
        return RestResultUtil.failed("密码重置失败");
    }
}
