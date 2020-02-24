package com.osen.aqms.web.camera;

import com.osen.aqms.common.result.RestResult;
import com.osen.aqms.common.utils.RestResultUtil;
import com.osen.aqms.modules.entity.system.Camera;
import com.osen.aqms.modules.service.CameraService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * User: PangYi
 * Date: 2020-02-24
 * Time: 8:49
 * Description:
 */
@RestController
@RequestMapping("${restful.prefix}")
@Slf4j
public class CameraController {

    @Autowired
    private CameraService cameraService;

    /**
     * 获取摄像头访问token
     *
     * @return 信息
     */
    @PostMapping("/camera/accesstoken")
    public RestResult getAccessToken() {
        String accessToken = cameraService.getAccessToken();
        return RestResultUtil.success(accessToken);
    }

    /**
     * 添加设备与摄像头关联
     *
     * @param camera 对象
     * @return 信息
     */
    @PostMapping("/camera/addToDevice")
    public RestResult addCameraToDevice(@RequestBody Camera camera) {
        boolean camera1 = cameraService.addCameraToDevice(camera);
        if (camera1)
            return RestResultUtil.success("摄像头添加成功");
        return RestResultUtil.failed("不允许重复添加同一台摄像头");
    }

    /**
     * 删除摄像头绑定
     *
     * @param camera 对象
     * @return 信息
     */
    @PostMapping("/camera/remove")
    public RestResult removeCamera(@RequestBody Camera camera) {
        boolean remove = cameraService.remove(camera);
        if (remove)
            return RestResultUtil.success("删除成功");
        return RestResultUtil.failed("删除失败");
    }

    /**
     * 获取设备号
     *
     * @param deviceNo 设备号
     * @return 信息
     */
    @PostMapping("/camera/list/{deviceNo}")
    public RestResult getCameraList(@PathVariable("deviceNo") String deviceNo) {
        List<Camera> cameraList = cameraService.getCameraList(deviceNo);
        return RestResultUtil.success(cameraList);
    }
}
