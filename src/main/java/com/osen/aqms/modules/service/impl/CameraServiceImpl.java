package com.osen.aqms.modules.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.osen.aqms.common.exception.type.ServiceException;
import com.osen.aqms.modules.entity.system.Camera;
import com.osen.aqms.modules.mapper.system.CameraMapper;
import com.osen.aqms.modules.service.CameraService;
import com.osen.aqms.web.camera.model.AccessTokenModel;
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
import java.util.List;
import java.util.concurrent.TimeUnit;

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
}
