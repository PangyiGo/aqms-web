package com.osen.aqms;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
class AqmsWebApplicationTests {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void contextLoads() {
        String osen_2019 = passwordEncoder.encode("OSEN_2019");
        System.out.println(osen_2019);

        String password = passwordEncoder.encode("123456");
        System.out.println(password);
    }

}
