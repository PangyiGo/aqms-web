package com.osen.aqms;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
// Mapper注解扫描
@MapperScan(basePackages = "com.osen.aqms.**.mapper*")
// 开启事务处理
@EnableTransactionManagement
// 开启异步处理
@EnableAsync
// 开启定时器任务
@EnableScheduling
public class AqmsWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(AqmsWebApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
