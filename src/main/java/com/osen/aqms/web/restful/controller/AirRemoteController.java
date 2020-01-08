package com.osen.aqms.web.restful.controller;

import com.osen.aqms.common.result.RestResult;
import com.osen.aqms.common.utils.ConstUtil;
import com.osen.aqms.common.utils.RestResultUtil;
import com.osen.aqms.web.restful.model.AirResponseModel;
import com.osen.aqms.web.restful.model.AirSensorVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("${restful.prefix}")
@Slf4j
public class AirRemoteController {

    @Autowired
    private RestTemplate restTemplate;

    /**
     * 数据参数校准
     *
     * @param airSensorVo 请求体
     * @return 信息
     */
    @PostMapping("/aqmsData/remote")
    public RestResult setAirSensorCommand(@RequestBody AirSensorVo airSensorVo) {
        String url = "http://127.0.0.1:8001/aqmsData/remote";
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<AirSensorVo> httpEntity = new HttpEntity<>(airSensorVo, httpHeaders);
        ResponseEntity<AirResponseModel> entity = restTemplate.postForEntity(url, httpEntity, AirResponseModel.class);
        if (entity.getStatusCode().is2xxSuccessful()) {
            AirResponseModel entityBody = entity.getBody();
            assert entityBody != null;
            if (ConstUtil.OK.equals(entityBody.getCode()))
                return RestResultUtil.success(entityBody.getMsg());
            else
                return RestResultUtil.failed(entityBody.getMsg());
        }
        return RestResultUtil.failed();
    }
}
