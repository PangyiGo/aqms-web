package com.osen.aqms.modules.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.osen.aqms.common.requestVo.DeviceSearchVo;
import com.osen.aqms.modules.entity.system.Camera;
import com.osen.aqms.web.camera.model.UserCameraModel;

import java.util.List;

/**
 * User: PangYi
 * Date: 2019-11-30
 * Time: 9:24
 * Description:
 */
public interface CameraService extends IService<Camera> {

    /**
     * 获取accesstoken
     *
     * @return 信息
     */
    String getAccessToken();

    /**
     * 添加设备与摄像头关联
     *
     * @param camera 对象
     * @return 信息
     */
    boolean addCameraToDevice(Camera camera);

    /**
     * 解除绑定摄像头
     *
     * @param camera 对象
     * @return 信息
     */
    boolean remove(Camera camera);

    /**
     * 获取设备号
     *
     * @param deviceNo 设备号
     * @return 信息
     */
    List<Camera> getCameraList(String deviceNo);

    /**
     * 获取当前用户的设备摄像头列表
     *
     * @return 信息
     */
    List<UserCameraModel> getUserCameraList(DeviceSearchVo deviceSearchVo);
}
