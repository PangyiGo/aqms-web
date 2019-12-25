package com.osen.aqms;

/**
 * User: PangYi
 * Date: 2019-12-10
 * Time: 10:11
 * Description:
 */
public class ApplicationTest {

    public static void main(String[] args) {
        for (int i = 0; i < 60; i++) {
            System.out.println("INSERT INTO data_his1912(device_id,datetime,VOC) VALUES ('2019112903106001'," + "'2019-12-13 13:" + i + ":00',0.1);");
        }
    }
}
