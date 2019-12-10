package com.osen.aqms.common.utils;

import java.time.LocalDateTime;

/**
 * User: PangYi
 * Date: 2019-12-02
 * Time: 14:30
 * Description:
 */
public class TableNameUtil {

    // 空气站实时数据表名
    public static String Air_history = "air_history";

    // 空气站分钟数据表名
    public static String Air_minute = "air_minute";

    // 空气站小时数据表名
    public static String Air_hour = "air_hour";

    // 空气站日数据表名
    public static String Air_day = "air_day";

    // 空气站报警数据表名
    public static String Air_alarm = "air_alarm";

    // AQI实时数据表名
    public static String Aqi_realtime = "aqi_realtime";

    // AQI小时数据表名
    public static String Aqi_hour = "aqi_hour";

    // AQI日数据表名
    public static String Aqi_day = "aqi_day";

    // 设备连接的Connection ID缓存键值
    public static String Air_Conn = "air_connection_db";

    // 设备实时最新数据键值
    public static String Air_Realtime = "air_realtime_db";

    // 设备实时报警键值
    public static String Air_Alarm = "air_alarm_db";

    public static String Msg_DeviceStatus = "msg_deviceStatus_db";

    public static String Msg_Alarm = "msg_alarm_db";

    public static String Msg_Warning = "msg_warning_db";

    /**
     * 动态生成当前月份表名
     *
     * @param baseName 原表名
     * @return 新表名
     */
    public static String nowTableName(String baseName) {
        LocalDateTime dateTime = LocalDateTime.now();
        int year = dateTime.getYear();
        int monthValue = dateTime.getMonthValue();
        String month = (monthValue < 10) ? "0" + monthValue : "" + monthValue;
        // 格式：基本表名_年月
        return baseName + "_" + year + month;
    }

    /**
     * 生成下一个月表名
     *
     * @param baseName 表名
     * @return 新表名
     */
    public static String nextTableName(String baseName) {
        LocalDateTime localDateTime = LocalDateTime.now();
        // 下一个月
        localDateTime = localDateTime.plusMonths(1);
        int year = localDateTime.getYear();
        int monthValue = localDateTime.getMonthValue();
        String month = (monthValue < 10) ? "0" + monthValue : "" + monthValue;
        // 格式：基本表名_年月
        return baseName + "_" + year + month;
    }

    /**
     * 获取上一个小时的数据表名称
     *
     * @param baseName 基本表名称
     * @return 生成后表名
     */
    public static String preHourTableName(String baseName) {
        LocalDateTime localDateTime = LocalDateTime.now();
        // 上一个小时
        localDateTime = localDateTime.minusHours(1);
        int year = localDateTime.getYear();
        int monthValue = localDateTime.getMonthValue();
        String month = (monthValue < 10) ? "0" + monthValue : "" + monthValue;
        // 格式：基本表名_年月
        return baseName + "_" + year + month;
    }

    /**
     * 获取上一个日的数据表名称
     *
     * @param baseName 基本表名称
     * @return 生成后表名
     */
    public static String preDayTableName(String baseName) {
        LocalDateTime localDateTime = LocalDateTime.now();
        // 上一个日的时间日期
        localDateTime = localDateTime.minusDays(1);
        int year = localDateTime.getYear();
        int monthValue = localDateTime.getMonthValue();
        String month = (monthValue < 10) ? "0" + monthValue : "" + monthValue;
        // 格式：基本表名_年月
        return baseName + "_" + year + month;
    }
}
