package com.osen.aqms;

import com.osen.aqms.modules.service.WebAddressService;
import com.osen.aqms.web.camera.model.AccessTokenModel;
import com.osen.aqms.web.restful.model.AirResponseModel;
import com.osen.aqms.web.restful.model.AirSensorVo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@SpringBootTest
class AqmsWebApplicationTests {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private WebAddressService webAddressService;

    @Value("${camera.appKey}")
    private String appKey;

    @Value("${camera.appSecret}")
    private String appSecret;

    @Test
    void contextLoads() {
        String osen_2019 = passwordEncoder.encode("OSEN_2019");
        System.out.println(osen_2019);

        String password = passwordEncoder.encode("123456");
        System.out.println(password);
    }

    @Test
    void test01() {

        String url = "http://127.0.0.1:8001/aqmsData/remote";

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        AirSensorVo airSensorVo = new AirSensorVo();
        airSensorVo.setDeviceNo("2019061403100011");
        airSensorVo.setNumber("100");
        airSensorVo.setSensor("NO2");

        HttpEntity<AirSensorVo> httpEntity = new HttpEntity<>(airSensorVo, httpHeaders);

        ResponseEntity<AirResponseModel> entity = restTemplate.postForEntity(url, httpEntity, AirResponseModel.class);

        System.out.println(entity.getBody());
    }

    @Test
    void test02() {
        String url = "https://open.ys7.com/api/lapp/token/get";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String,Object> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.set("appKey", appKey);
        multiValueMap.set("appSecret",appSecret);

        HttpEntity<MultiValueMap<String,Object>> httpEntity = new HttpEntity<>(multiValueMap, headers);

        ResponseEntity<AccessTokenModel> responseEntity = restTemplate.postForEntity(url, httpEntity, AccessTokenModel.class);

        System.out.println(responseEntity.getBody());
    }
}
