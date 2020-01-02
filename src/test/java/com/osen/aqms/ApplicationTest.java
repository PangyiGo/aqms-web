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

        LocalDateTime dateTime = LocalDateTime.now();
        LocalDateTime endTime = LocalDateTime.of(dateTime.getYear(), dateTime.getMonthValue(), dateTime.getDayOfMonth(), 0, 0, 0);
        LocalDateTime startTime = endTime.minusWeeks(1);

        System.out.println(endTime);

        System.out.println(startTime);
    }
}
