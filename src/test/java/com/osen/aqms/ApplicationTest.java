package com.osen.aqms;

import cn.hutool.core.util.StrUtil;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * User: PangYi
 * Date: 2019-12-10
 * Time: 10:11
 * Description:
 */
public class ApplicationTest {

    public static void main(String[] args) {
        String name = "aqi_hour_201912";

        System.out.println(StrUtil.sub(name, 9, 13));

        System.out.println(StrUtil.sub(name, 13, name.length()));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        LocalDateTime localDateTime = LocalDateTime.parse("2019-12-20",formatter);

        System.out.println(localDateTime);
    }
}
