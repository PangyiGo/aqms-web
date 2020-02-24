package com.osen.aqms.modules.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.osen.aqms.common.exception.type.ServiceException;
import com.osen.aqms.common.model.DeviceListDataModel;
import com.osen.aqms.common.requestVo.UserGetVo;
import com.osen.aqms.modules.entity.system.Camera;
import com.osen.aqms.modules.entity.system.Device;
import com.osen.aqms.modules.mapper.system.CameraMapper;
import com.osen.aqms.modules.service.CameraService;
import com.osen.aqms.modules.service.DeviceService;
import com.osen.aqms.web.camera.model.AccessTokenModel;
import com.osen.aqms.web.camera.model.UserCameraModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * User: PangYi
 * Date: 2019-11-30
 * Time: 10:52
 * Description:
 */
@Service
public class CameraServiceImpl extends ServiceImpl<CameraMapper, Camera> implements CameraService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${camera.appKey}")
    private String appKey;

    @Value("${camera.appSecret}")
    private String appSecret;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private static final String ACCESSTOKEN = "ACCESSTOKEN";

    @Autowired
    private DeviceService deviceService;

    @Override
    public String getAccessToken() {
        // 获取token是否过期
        Long expire = stringRedisTemplate.boundValueOps(ACCESSTOKEN).getExpire();
        if (expire != null && expire > 0) {
            return stringRedisTemplate.boundValueOps(ACCESSTOKEN).get();
        } else {
            // 重新获取token
            String url = "https://open.ys7.com/api/lapp/token/get";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            MultiValueMap<String, Object> multiValueMap = new LinkedMultiValueMap<>();
            multiValueMap.set("appKey", appKey);
            multiValueMap.set("appSecret", appSecret);
            HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(multiValueMap, headers);
            ResponseEntity<AccessTokenModel> responseEntity = restTemplate.postForEntity(url, httpEntity, AccessTokenModel.class);
            AccessTokenModel entityBody = responseEntity.getBody();
            if (ObjectUtil.isNotEmpty(entityBody)) {
                assert entityBody != null;
                if ("200".equals(entityBody.getCode())) {
                    // 将token存储到缓存
                    stringRedisTemplate.boundValueOps(ACCESSTOKEN).set(entityBody.getData().getAccessToken(), entityBody.getData().getExpireTime(), TimeUnit.MILLISECONDS);
                    // 返回
                    return entityBody.getData().getAccessToken();
                } else {
                    return entityBody.getMsg();
                }
            } else {
                throw new ServiceException("获取accesstoken值失败");
            }
        }
    }

    @Override
    public boolean addCameraToDevice(Camera camera) {
        // 序列号唯一
        LambdaQueryWrapper<Camera> wrapper = Wrappers.<Camera>lambdaQuery().eq(Camera::getDeviceNo, camera.getDeviceNo()).eq(Camera::getSerial, camera.getSerial());
        Camera camera1 = super.getOne(wrapper);
        if (camera1 != null)
            return false;
        camera.setCreateTime(LocalDateTime.now());
        camera.setUpdateTime(LocalDateTime.now());
        camera.setCameraName(camera.getDeviceNo());
        return super.save(camera);
    }

    @Override
    public boolean remove(Camera camera) {
        LambdaQueryWrapper<Camera> wrapper = Wrappers.<Camera>lambdaQuery().eq(Camera::getDeviceNo, camera.getDeviceNo()).eq(Camera::getSerial, camera.getSerial());
        return super.remove(wrapper);
    }

    @Override
    public List<Camera> getCameraList(String deviceNo) {
        LambdaQueryWrapper<Camera> wrapper = Wrappers.<Camera>lambdaQuery().eq(Camera::getDeviceNo, deviceNo);
        List<Camera> cameraList = super.list(wrapper);
        if (cameraList == null) {
            return new ArrayList<Camera>(0);
        }
        return cameraList;
    }

    @Override
    public Map<String, Object> getUserCameraList(UserGetVo userGetVo) {
        Map<String, Object> map = new HashMap<>(0);
        List<UserCameraModel> userCameraModelList = new ArrayList<>(0);
        // 获取用户设备列表
        DeviceListDataModel pageToCurrentUser = deviceService.findDeviceListPageToCurrentUser(userGetVo);
        if (pageToCurrentUser.getTotal() == 0) {
            map.put("total", 0);
            map.put("cameras", userCameraModelList);
            return map;
        }
        List<String> deviceNos = new ArrayList<>(0);
        pageToCurrentUser.getUserList().forEach(device -> {
            deviceNos.add(device.getDeviceNo());
        });
        // 查询摄像头列表
        LambdaQueryWrapper<Camera> wrapper = Wrappers.<Camera>lambdaQuery().in(Camera::getDeviceNo, deviceNos);
        List<Camera> cameraList = super.list(wrapper);
        if (cameraList == null) {
            map.put("total", 0);
            map.put("cameras", userCameraModelList);
            return map;
        }
        for (Device device : pageToCurrentUser.getUserList()) {
            // 设备是有摄像头
            List<Camera> cameras = cameraList.stream().filter(camera -> camera.getDeviceNo().equals(device.getDeviceNo())).collect(Collectors.toList());
            if (cameras.size() > 0) {
                cameras.forEach(camera -> {
                    UserCameraModel model = new UserCameraModel();
                    BeanUtil.copyProperties(device, model);
                    model.setSerial(camera.getSerial());
                    userCameraModelList.add(model);
                });
            }
        }
        if (userCameraModelList.size() > 0) {
            map.put("total", userCameraModelList.size());
            map.put("cameras", userCameraModelList);
        } else {
            map.put("total", 0);
            map.put("cameras", userCameraModelList);
        }
        return map;
    }
}
