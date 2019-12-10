package com.osen.aqms.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * User: PangYi
 * Date: 2019-12-09
 * Time: 16:17
 * Description:
 */
@RestController
public class TestController {

    @GetMapping("/test")
    public String test(){
        return "test";
    }
}
