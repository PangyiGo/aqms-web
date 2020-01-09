package com.osen.aqms;

import com.osen.aqms.modules.entity.address.WebAddress;
import com.osen.aqms.modules.service.WebAddressService;
import com.osen.aqms.web.restful.model.AirResponseModel;
import com.osen.aqms.web.restful.model.AirSensorVo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@SpringBootTest
class AqmsWebApplicationTests {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private WebAddressService webAddressService;

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
    void test02(){
        List<WebAddress> list = webAddressService.list();

        list.forEach(System.out::println);
    }
}
