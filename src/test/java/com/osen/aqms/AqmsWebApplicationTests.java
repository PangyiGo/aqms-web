package com.osen.aqms;

import com.osen.aqms.common.utils.ConstUtil;
import com.osen.aqms.modules.entity.system.User;
import com.osen.aqms.modules.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@SpringBootTest
class AqmsWebApplicationTests {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @Test
    void contextLoads() {
        String osen_2019 = passwordEncoder.encode("OSEN_2019");
        System.out.println(osen_2019);

        String password = "$2a$10$tDccCl7gp41EmDwuCmGDmuSDjPnqT0tdzE.9DG2.l6kxgcOwVlibe";

        User user = new User();
        user.setAccount("OSEN_2019");
        user.setPassword(password);
        user.setCustomer("系统超级管理员");
        user.setPhone("15920362472");
        user.setCompany("深圳奥斯恩净化技术有限公司");
        user.setAddress("广东省深圳市宝安区福永街道凤凰工业园");
        user.setRemark("备注信息");
        user.setStatus(ConstUtil.OPEN_STATUS);
        user.setParentId(0);
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());

        userService.save(user);
    }

}
