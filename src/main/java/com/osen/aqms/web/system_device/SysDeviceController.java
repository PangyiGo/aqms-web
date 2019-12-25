package com.osen.aqms.web.system_device;

import com.osen.aqms.common.model.DeviceListDataModel;
import com.osen.aqms.common.model.DeviceStatusModel;
import com.osen.aqms.common.model.DeviceTreeModel;
import com.osen.aqms.common.requestVo.AccountDeviceVo;
import com.osen.aqms.common.requestVo.AddressVo;
import com.osen.aqms.common.requestVo.UserGetVo;
import com.osen.aqms.common.result.RestResult;
import com.osen.aqms.common.utils.RestResultUtil;
import com.osen.aqms.common.utils.SecurityUtil;
import com.osen.aqms.modules.entity.system.Device;
import com.osen.aqms.modules.service.DeviceService;
import com.osen.aqms.modules.service.UserDeviceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * User: PangYi
 * Date: 2019-12-17
 * Time: 15:17
 * Description:
 */
@RestController
@RequestMapping("${restful.prefix}")
@Slf4j
public class SysDeviceController {

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private UserDeviceService userDeviceService;

    /**
     * 根据登录用户获取设备的树形列表
     *
     * @return 信息
     */
    @PostMapping("/device/treeList")
    public RestResult getDeviceTreeToUsername() {
        // 当前登录用户名
        String username = SecurityUtil.getUsername();
        List<DeviceTreeModel> treeList = deviceService.findDeviceTreeListToUsername(username);
        // 返回
        return RestResultUtil.success(treeList);
    }

    /**
     * 根据设备号查询设备详细信息
     *
     * @return 信息
     */
    @PostMapping("/device/infoDetail/{deviceNo}")
    public RestResult getDeviceInfo(@PathVariable("deviceNo") String deviceNo) {
        Device oneDeviceToNo = deviceService.findOneDeviceToNo(deviceNo);
        if (oneDeviceToNo == null)
            return RestResultUtil.failed("无法查询设备");
        return RestResultUtil.success(oneDeviceToNo);
    }

    /**
     * 根据区域名称和区域等级获取设备号信息
     *
     * @param addressVo 请求体
     * @return 信息
     */
    @PostMapping("/device/group")
    public RestResult getDeviceToGroupByAddress(@RequestBody AddressVo addressVo) {
        List<Device> devices = deviceService.findDeviceGroupByAddress(addressVo.getAddress(), addressVo.getLevel());
        return RestResultUtil.success(devices);
    }

    /**
     * 获取当前用户的设备状态信息
     *
     * @param number 分页号
     * @return 信息
     */
    @PostMapping("/device/status/{number}")
    public RestResult getDeviceStatus(@PathVariable("number") String number) {
        DeviceStatusModel deviceStatus = deviceService.findDeviceStatus(number);
        return RestResultUtil.success(deviceStatus);
    }

    /**
     * 不分页查询指定账号的设备列表
     *
     * @param account 账号
     * @return 信息
     */
    @PostMapping("/device/userList/{account}")
    public RestResult getUserDeviceNoPage(@PathVariable("account") String account) {
        List<Device> deviceList = deviceService.findDeviceAllToUsername(account);
        return RestResultUtil.success(deviceList);
    }

    /**
     * 分页查询当前设备列表
     *
     * @param userGetVo 请求体
     * @return 信息
     */
    @PostMapping("/device/currentUser")
    public RestResult getDeviceListPageToCurrentUser(@RequestBody UserGetVo userGetVo) {
        DeviceListDataModel pageToCurrentUser = deviceService.findDeviceListPageToCurrentUser(userGetVo);
        return RestResultUtil.success(pageToCurrentUser);
    }

    /**
     * 指定设备与指定账号关联或取消关联
     *
     * @param accountDeviceVo 请求体
     * @param type            conn 表示关联 cancel 表示取消关联
     * @return 信息
     */
    @PostMapping("/userDevice/{type}")
    public RestResult updateUserToDeviceStatus(@RequestBody AccountDeviceVo accountDeviceVo, @PathVariable("type") String type) {
        boolean status = userDeviceService.updateUserToDeviceStatus(accountDeviceVo, type);
        String tips;
        if (type.equals("conn"))
            tips = "账号与设备绑定";
        else
            tips = "账号与设备解除绑定";
        if (status)
            return RestResultUtil.success(tips + "成功");
        else
            return RestResultUtil.failed(tips + "失败");
    }

    /**
     * 删除指定设备号的设备
     *
     * @param deviceNo 设备号
     * @return 信息
     */
    @PostMapping("/device/delete/{deviceNo}")
    public RestResult deleteToDeviceNo(@PathVariable("deviceNo") String deviceNo) {
        String delete = deviceService.deleteToDeviceNo(deviceNo);
        return RestResultUtil.success(delete);
    }
}
