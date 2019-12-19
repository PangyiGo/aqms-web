package com.osen.aqms.common.utils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * User: PangYi
 * Date: 2019-09-07
 * Time: 10:00
 * Description: 日期时间转换工具
 */
public class DateTimeUtil {

    /**
     * LocalDate 转化 Date
     *
     * @param localDate 日期
     * @return 结果
     */
    public static Date asDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * LocalDateTime 转化 Date
     *
     * @param localDateTime 日期
     * @return 结果
     */
    public static Date asDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * Date 转化 LocalDate
     *
     * @param date 日期
     * @return 结果
     */
    public static LocalDate asLocalDate(Date date) {
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
    }

    /**
     * Date 转化 LocalDateTime
     *
     * @param date 日期
     * @return 结果
     */
    public static LocalDateTime asLocalDateTime(Date date) {
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    /**
     * 查询时间格式化
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 信息
     */
    public static List<LocalDateTime> queryTimeFormatter(String startTime, String endTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(ConstUtil.QUERY_DATE);
        LocalDate start = LocalDate.parse(startTime, formatter);
        LocalDate end = LocalDate.parse(endTime, formatter);
        LocalDateTime startDate = LocalDateTime.of(start.getYear(), start.getMonthValue(), start.getDayOfMonth(), 0, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(end.getYear(), end.getMonth(), end.getDayOfMonth(), 23, 59, 59);
        // 返回
        List<LocalDateTime> localDateTimes = new ArrayList<>(0);
        localDateTimes.add(startDate);
        localDateTimes.add(endDate);
        return localDateTimes;
    }
}
