package com.osen.aqms.web.message;

import com.osen.aqms.common.result.RestResult;
import com.osen.aqms.common.utils.RestResultUtil;
import com.osen.aqms.common.utils.SecurityUtil;
import com.osen.aqms.modules.entity.message.MessageDevice;
import com.osen.aqms.modules.service.MessageDeviceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * User: PangYi
 * Date: 2019-12-18
 * Time: 9:36
 * Description:
 */
@RestController
@RequestMapping("${restful.prefix}")
@Slf4j
public class MessageDeviceController {

    @Autowired
    private MessageDeviceService messageDeviceService;

    /**
     * 获取当前用户的设备状态信息
     *
     * @return 信息
     */
    @PostMapping("/message/deviceInfo")
    public RestResult getMessageDeviceToUsername() {
        // 用户名
        String username = SecurityUtil.getUsername();
        List<MessageDevice> messageDeviceList = messageDeviceService.getMessageToUsername(username);
        return RestResultUtil.success(messageDeviceList);
    }

    /**
     * 根据设备信息类型和设备号，清除相应的设备信息缓存
     *
     * @param type     设备信息类型
     * @param deviceNo 设备号
     * @return 信息
     */
    @PostMapping("/message/clear/{type}/{deviceNo}")
    public RestResult clearMessageDeviceInfo(@PathVariable("type") String type, @PathVariable("deviceNo") String deviceNo) {
        messageDeviceService.deleteMessageToTypeAndDeviceNo(type, deviceNo);
        return RestResultUtil.success("设备信息标记已读");
    }

}
