package com.osen.aqms.common.utils;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * User: PangYi
 * Date: 2019-12-02
 * Time: 14:30
 * Description:
 */
@Slf4j
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

    // 设备树形节点列表
    public static String DeviceTree_List = "device_tree_db";

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

    /**
     * 获取查询表名列表
     *
     * @param baseName  基本表名
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 信息
     */
    public static List<String> tableNameList(String baseName, String startTime, String endTime) {
        List<String> tableNames = new ArrayList<>(0);
        // 时间格式化
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(ConstUtil.QUERY_DATE);
        // 开始时间
        LocalDate startDateTime;
        // 结束时间
        LocalDate endDateTime;
        try {
            startDateTime = LocalDate.parse(startTime, dateTimeFormatter);
            endDateTime = LocalDate.parse(endTime, dateTimeFormatter);
        } catch (Exception e) {
            log.error("Query time format exception");
            log.error(ThrowableUtil.getStackTrace(e));
            return tableNames;
        }
        // 最早时间
        LocalDate initDate = LocalDate.of(2019, 12, 1);
        if (initDate.isAfter(startDateTime)) {
            startDateTime = initDate;
        }
        // 最晚时间
        LocalDate end = LocalDate.now();
        if (end.isBefore(endDateTime)) {
            endDateTime = end;
        }
        // 月份时间差
        int minMonth = (endDateTime.getYear() - startDateTime.getYear()) * 12 + (endDateTime.getMonthValue() - startDateTime.getMonthValue());
        // 最开始时间日期
        LocalDate localDate = LocalDate.of(startDateTime.getYear(), startDateTime.getMonthValue(), 1);
        for (int i = 0; i <= minMonth; i++) {
            String month = (localDate.getMonthValue() < 10) ? "0" + localDate.getMonthValue() : "" + localDate.getMonthValue();
            String year = localDate.getYear() + "";
            String tableName = baseName + "_" + year + month;
            tableNames.add(tableName);
            // 下一个月
            localDate = localDate.plusMonths(1);
        }
        return tableNames;
    }

    /**
     * 获取查询表名列表
     *
     * @param baseName  基本表名
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 信息
     */
    public static List<String> tableNameList(String baseName, LocalDateTime startTime, LocalDateTime endTime) {
        // 转换为 yyyy-MM-dd 格式
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(ConstUtil.QUERY_DATE);
        String start = startTime.format(dateTimeFormatter);
        String end = endTime.format(dateTimeFormatter);
        return tableNameList(baseName, start, end);
    }

    /**
     * 获取查询表名列表
     *
     * @param baseName  基本表名
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 信息
     */
    public static List<String> tableNameList(String baseName, LocalDate startTime, LocalDate endTime) {
        // 转换为 yyyy-MM-dd 格式
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(ConstUtil.QUERY_DATE);
        String start = startTime.format(dateTimeFormatter);
        String end = endTime.format(dateTimeFormatter);
        return tableNameList(baseName, start, end);
    }

    /**
     * 根据时间日期生成对应的日期数据表
     *
     * @param baseName 基本表名称
     * @param datetime 日期
     * @return 信息
     */
    public static String generateTableName(String baseName, String datetime, String formate) {
        // 时间格式化
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(formate);
        LocalDate localDate = LocalDate.parse(datetime, formatter);
        // 最早时间
        LocalDate initDate = LocalDate.of(2019, 12, 1);
        if (initDate.isAfter(localDate)) {
            localDate = initDate;
        }
        // 生成表
        String month = (localDate.getMonthValue() < 10) ? "0" + localDate.getMonthValue() : "" + localDate.getMonthValue();
        String year = localDate.getYear() + "";
        return baseName + "_" + year + month;
    }
}
