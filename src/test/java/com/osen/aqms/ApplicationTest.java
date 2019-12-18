package com.osen.aqms;

import java.time.LocalDateTime;

/**
 * User: PangYi
 * Date: 2019-12-10
 * Time: 10:11
 * Description:
 */
public class ApplicationTest {

    public static void main(String[] args) {
        LocalDateTime now = LocalDateTime.of(2020, 1, 1, 8, 0, 0);

        LocalDateTime endTime = LocalDateTime.of(now.getYear(), now.getMonthValue(), now.getDayOfMonth(), now.getHour(), 0,0);

        LocalDateTime tmp = endTime;
        for (int i = 0; i < 24; i++) {
            tmp = tmp.minusHours(1);
            System.out.println(tmp);
        }

        System.out.println("------------------");
        System.out.println(endTime.minusHours(24));
    }
}
