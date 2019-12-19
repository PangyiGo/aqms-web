package com.osen.aqms.common.utils;

import com.osen.aqms.common.enums.AirSensor;
import com.osen.aqms.common.enums.AqiStandard;
import com.osen.aqms.common.model.AirAvgModel;
import com.osen.aqms.modules.entity.data.AirHistory;
import com.osen.aqms.modules.entity.data.AqiDay;
import com.osen.aqms.modules.entity.data.AqiHour;
import com.osen.aqms.modules.entity.data.AqiRealtime;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * User: PangYi
 * Date: 2019-12-03
 * Time: 17:07
 * Description: AQI计算工具类
 */
public class AQIComputedUtil {

    /**
     * @param data 浓度值
     * @return IAQI值，对应分指数
     */
    private static int pm25_24h(BigDecimal data) {
        int temp = data.intValue();
        double res = 0;
        if (temp >= 0 && temp < 30) {
            res = (50 / 35.0) * temp;
        } else if (temp >= 30 && temp < 75) {
            res = (50 / 40.0) * (temp - 35) + 50;
        } else if (temp >= 75 && temp < 115) {
            res = (50 / 40.0) * (temp - 75) + 100;
        } else if (temp >= 115 && temp < 150) {
            res = (50 / 35.0) * (temp - 115) + 150;
        } else if (temp >= 150 && temp < 250) {
            res = (100 / 100.0) * (temp - 150) + 200;
        } else if (temp >= 250 && temp < 350) {
            res = (100 / 100.0) * (temp - 250) + 300;
        } else if (temp >= 350 && temp <= 500) {
            res = (100 / 150.0) * (temp - 350) + 400;
        }
        return (int) Math.ceil(res);
    }

    /**
     * @param data 浓度值
     * @return IAQI值，对应分指数
     */
    private static int pm10_24h(BigDecimal data) {
        int temp = data.intValue();
        double res = 0;
        if (temp >= 0 && temp < 50) {
            res = (50 / 50.0) * temp;
        } else if (temp >= 50 && temp < 150) {
            res = (50 / 100.0) * (temp - 50) + 50;
        } else if (temp >= 150 && temp < 250) {
            res = (50 / 100.0) * (temp - 150) + 100;
        } else if (temp >= 250 && temp < 350) {
            res = (50 / 100.0) * (temp - 250) + 150;
        } else if (temp >= 350 && temp < 420) {
            res = (100 / 70.0) * (temp - 350) + 200;
        } else if (temp >= 420 && temp < 500) {
            res = (100 / 80.0) * (temp - 420) + 300;
        } else if (temp >= 500 && temp <= 600) {
            res = (100 / 100.0) * (temp - 500) + 400;
        }
        return (int) Math.ceil(res);
    }

    /**
     * @param data 浓度值
     * @return IAQI值，对应分指数
     */
    private static int so2_24h(BigDecimal data) {
        int temp = data.intValue();
        double res = 0;
        if (temp >= 0 && temp < 50) {
            res = (50 / 50.0) * temp;
        } else if (temp >= 50 && temp < 150) {
            res = (50 / 100.0) * (temp - 50) + 50;
        } else if (temp >= 150 && temp < 475) {
            res = (50 / 325.0) * (temp - 150) + 100;
        } else if (temp >= 475 && temp < 800) {
            res = (50 / 325.0) * (temp - 475) + 150;
        } else if (temp >= 800 && temp < 1600) {
            res = (100 / 800.0) * (temp - 800) + 200;
        } else if (temp >= 1600 && temp < 2100) {
            res = (100 / 500.0) * (temp - 1600) + 300;
        } else if (temp >= 2100 && temp <= 2620) {
            res = (100 / 520.0) * (temp - 2100) + 400;
        }
        return (int) Math.ceil(res);
    }

    /**
     * @param data 浓度值
     * @return IAQI值，对应分指数
     */
    private static int so2_1h(BigDecimal data) {
        int temp = data.intValue();
        if (temp > 800)
            return 0;
        double res = 0;
        if (temp >= 0 && temp < 150) {
            res = (50 / 150.0) * temp;
        } else if (temp >= 150 && temp < 500) {
            res = (50 / 350.0) * (temp - 150) + 50;
        } else if (temp >= 500 && temp < 650) {
            res = (50 / 150.0) * (temp - 500) + 100;
        } else if (temp >= 650 && temp < 800) {
            res = (50 / 150.0) * (temp - 650) + 150;
        }
        return (int) Math.ceil(res);
    }

    /**
     * @param data 浓度值
     * @return IAQI值，对应分指数
     */
    private static int no2_24h(BigDecimal data) {
        int temp = data.intValue();
        double res = 0;
        if (temp >= 0 && temp < 40) {
            res = (50 / 40.0) * temp;
        } else if (temp >= 40 && temp < 80) {
            res = (50 / 40.0) * (temp - 40) + 50;
        } else if (temp >= 80 && temp < 180) {
            res = (50 / 100.0) * (temp - 80) + 100;
        } else if (temp >= 180 && temp < 280) {
            res = (50 / 100.0) * (temp - 180) + 150;
        } else if (temp >= 280 && temp < 565) {
            res = (100 / 285.0) * (temp - 280) + 200;
        } else if (temp >= 565 && temp < 750) {
            res = (100 / 185.0) * (temp - 565) + 300;
        } else if (temp >= 750 && temp <= 940) {
            res = (100 / 190.0) * (temp - 750) + 400;
        }
        return (int) Math.ceil(res);
    }

    /**
     * @param data 浓度值
     * @return IAQI值，对应分指数
     */
    private static int no2_1h(BigDecimal data) {
        int temp = data.intValue();
        double res = 0;
        if (temp >= 0 && temp < 100) {
            res = (50 / 100.0) * temp;
        } else if (temp >= 100 && temp < 200) {
            res = (50 / 100.0) * (temp - 100) + 50;
        } else if (temp >= 200 && temp < 700) {
            res = (50 / 500.0) * (temp - 200) + 100;
        } else if (temp >= 700 && temp < 1200) {
            res = (50 / 500.0) * (temp - 700) + 150;
        } else if (temp >= 1200 && temp < 2340) {
            res = (100 / 1140.0) * (temp - 1200) + 200;
        } else if (temp >= 2340 && temp < 3090) {
            res = (100 / 750.0) * (temp - 2340) + 300;
        } else if (temp >= 3090 && temp <= 3840) {
            res = (100 / 750.0) * (temp - 3090) + 400;
        }
        return (int) Math.ceil(res);
    }

    /**
     * @param data 浓度值
     * @return IAQI值，对应分指数
     */
    private static int co_24h(BigDecimal data) {
        int temp = data.intValue();
        double res = 0;
        if (temp >= 0 && temp < 2) {
            res = (50 / 2.0) * temp;
        } else if (temp >= 2 && temp < 4) {
            res = (50 / 2.0) * (temp - 2) + 50;
        } else if (temp >= 4 && temp < 14) {
            res = (50 / 10.0) * (temp - 4) + 100;
        } else if (temp >= 14 && temp < 24) {
            res = (50 / 10.0) * (temp - 14) + 150;
        } else if (temp >= 24 && temp < 36) {
            res = (100 / 12.0) * (temp - 24) + 200;
        } else if (temp >= 36 && temp < 48) {
            res = (100 / 12.0) * (temp - 36) + 300;
        } else if (temp >= 48 && temp <= 60) {
            res = (100 / 12.0) * (temp - 48) + 400;
        }
        return (int) Math.ceil(res);
    }

    /**
     * @param data 浓度值
     * @return IAQI值，对应分指数
     */
    private static int co_1h(BigDecimal data) {
        int temp = data.intValue();
        double res = 0;
        if (temp >= 0 && temp < 5) {
            res = (50 / 5.0) * temp;
        } else if (temp >= 5 && temp < 10) {
            res = (50 / 5.0) * (temp - 5) + 50;
        } else if (temp >= 10 && temp < 35) {
            res = (50 / 25.0) * (temp - 10) + 100;
        } else if (temp >= 35 && temp < 60) {
            res = (50 / 25.0) * (temp - 35) + 150;
        } else if (temp >= 60 && temp < 90) {
            res = (100 / 30.0) * (temp - 60) + 200;
        } else if (temp >= 90 && temp < 120) {
            res = (100 / 30.0) * (temp - 90) + 300;
        } else if (temp >= 120 && temp <= 150) {
            res = (100 / 30.0) * (temp - 120) + 400;
        }
        return (int) Math.ceil(res);
    }

    /**
     * @param data 浓度值
     * @return IAQI值，对应分指数
     */
    private static int o3_8h(BigDecimal data) {
        int temp = data.intValue();
        if (temp > 800)
            return 0;
        double res = 0;
        if (temp >= 0 && temp < 100) {
            res = (50 / 100.0) * temp;
        } else if (temp >= 100 && temp < 160) {
            res = (50 / 60.0) * (temp - 100) + 50;
        } else if (temp >= 160 && temp < 215) {
            res = (50 / 55.0) * (temp - 160) + 100;
        } else if (temp >= 215 && temp < 265) {
            res = (50 / 50.0) * (temp - 215) + 150;
        } else if (temp >= 265 && temp < 800) {
            res = (100 / 535.0) * (temp - 265) + 200;
        }
        return (int) Math.ceil(res);
    }

    /**
     * @param data 浓度值
     * @return IAQI值，对应分指数
     */
    private static int o3_1h(BigDecimal data) {
        int temp = data.intValue();
        double res = 0;
        if (temp >= 0 && temp < 160) {
            res = (50 / 160.0) * temp;
        } else if (temp >= 160 && temp < 200) {
            res = (50 / 40.0) * (temp - 160) + 50;
        } else if (temp >= 200 && temp < 300) {
            res = (50 / 100.0) * (temp - 200) + 100;
        } else if (temp >= 300 && temp < 400) {
            res = (50 / 100.0) * (temp - 300) + 150;
        } else if (temp >= 400 && temp < 800) {
            res = (100 / 400.0) * (temp - 400) + 200;
        } else if (temp >= 800 && temp < 1000) {
            res = (100 / 200.0) * (temp - 800) + 300;
        } else if (temp >= 1000 && temp <= 1200) {
            res = (100 / 200.0) * (temp - 1000) + 400;
        }
        return (int) Math.ceil(res);
    }

    /**
     * 计算实时AQI指数值
     *
     * @param airHistory 实时数据
     * @return 信息
     */
    public static AqiRealtime computedAqiToRealtime(AirHistory airHistory) {
        int maxIAQI = -1; // 最大IAQI，即是AQI值
        String primary = "-"; // 首要污染物
        BigDecimal data = new BigDecimal(0); // 首要污染物浓度值
        AqiRealtime aqiRealtime = new AqiRealtime();
        // 计算最大AQI和首要污染物值
        for (AirSensor airSensor : AirSensor.values()) {
            switch (airSensor) {
                case PM25:
                    int pm25_24h = AQIComputedUtil.pm25_24h(airHistory.getPm25());
                    if (pm25_24h > maxIAQI) {
                        maxIAQI = pm25_24h;
                        primary = airSensor.getDesc();
                        data = airHistory.getPm25();
                    }
                    break;
                case PM10:
                    int pm10_24h = AQIComputedUtil.pm10_24h(airHistory.getPm10());
                    if (pm10_24h > maxIAQI) {
                        maxIAQI = pm10_24h;
                        primary = airSensor.getDesc();
                        data = airHistory.getPm10();
                    }
                    break;
                case SO2:
                    int so2_1h = AQIComputedUtil.so2_1h(airHistory.getSo2());
                    if (so2_1h > maxIAQI) {
                        maxIAQI = so2_1h;
                        primary = airSensor.getDesc();
                        data = airHistory.getSo2();
                    }
                    break;
                case NO2:
                    int no2_1h = AQIComputedUtil.no2_1h(airHistory.getNo2());
                    if (no2_1h > maxIAQI) {
                        maxIAQI = no2_1h;
                        primary = airSensor.getDesc();
                        data = airHistory.getNo2();
                    }
                    break;
                case CO:
                    int co_1h = AQIComputedUtil.co_1h(airHistory.getCo());
                    if (co_1h > maxIAQI) {
                        maxIAQI = co_1h;
                        primary = airSensor.getDesc();
                        data = airHistory.getCo();
                    }
                    break;
                case O3:
                    int o3_1h = AQIComputedUtil.o3_1h(airHistory.getO3());
                    if (o3_1h > maxIAQI) {
                        maxIAQI = o3_1h;
                        primary = airSensor.getDesc();
                        data = airHistory.getO3();
                    }
                    break;
            }
        }
        // 计算空气质量等级
        for (AqiStandard aqiStandard : AqiStandard.values()) {
            if (maxIAQI >= aqiStandard.getMin() && maxIAQI <= aqiStandard.getMax()) {
                aqiRealtime.setQuality(aqiStandard.getQuality());
                aqiRealtime.setLevel(aqiStandard.getLevel());
                aqiRealtime.setTips(aqiStandard.getTips());
            }
        }
        // IAQI值是否大于50
        if (maxIAQI <= 50) {
            primary = "-";
            data = null;
        }
        aqiRealtime.setAirId(airHistory.getId());
        aqiRealtime.setDeviceNo(airHistory.getDeviceNo());
        aqiRealtime.setDateTime(airHistory.getDateTime());
        aqiRealtime.setAqi(maxIAQI);
        aqiRealtime.setData(data);
        aqiRealtime.setPollute(primary);
        // 返回
        return aqiRealtime;
    }

    /**
     * 计算AQI小时值
     *
     * @param deviceNo          设备号
     * @param dateTime          接收时间
     * @param airAvgToHourModel 空气站小时平均值
     * @return 信息
     */
    public static AqiHour computedAqiToHour(String deviceNo, LocalDateTime dateTime, AirAvgModel airAvgToHourModel) {
        // AQI小时实体
        AqiHour aqiHour = new AqiHour();
        int maxIAQI = -1; // 最大IAQI，即是AQI值
        String primary = "-"; // 首要污染物
        BigDecimal data = new BigDecimal(0); // 首要污染物浓度值
        // 计算最大AQI和首要污染物值
        for (AirSensor airSensor : AirSensor.values()) {
            switch (airSensor) {
                case PM25:
                    int pm25_24h = AQIComputedUtil.pm25_24h(airAvgToHourModel.getPm25Avg());
                    // 分指数
                    aqiHour.setPm25Index(pm25_24h);
                    if (pm25_24h > maxIAQI) {
                        maxIAQI = pm25_24h;
                        primary = airSensor.getDesc();
                        data = airAvgToHourModel.getPm25Avg();
                    }
                    break;
                case PM10:
                    int pm10_24h = AQIComputedUtil.pm10_24h(airAvgToHourModel.getPm10Avg());
                    // 分指数
                    aqiHour.setPm10Index(pm10_24h);
                    if (pm10_24h > maxIAQI) {
                        maxIAQI = pm10_24h;
                        primary = airSensor.getDesc();
                        data = airAvgToHourModel.getPm10Avg();
                    }
                    break;
                case SO2:
                    int so2_1h = AQIComputedUtil.so2_1h(airAvgToHourModel.getSo2Avg());
                    // 分指数
                    aqiHour.setSo2Index(so2_1h);
                    if (so2_1h > maxIAQI) {
                        maxIAQI = so2_1h;
                        primary = airSensor.getDesc();
                        data = airAvgToHourModel.getSo2Avg();
                    }
                    break;
                case NO2:
                    int no2_1h = AQIComputedUtil.no2_1h(airAvgToHourModel.getNo2Avg());
                    // 分指数
                    aqiHour.setNo2Index(no2_1h);
                    if (no2_1h > maxIAQI) {
                        maxIAQI = no2_1h;
                        primary = airSensor.getDesc();
                        data = airAvgToHourModel.getNo2Avg();
                    }
                    break;
                case CO:
                    int co_1h = AQIComputedUtil.co_1h(airAvgToHourModel.getCoAvg());
                    // 分指数
                    aqiHour.setCoIndex(co_1h);
                    if (co_1h > maxIAQI) {
                        maxIAQI = co_1h;
                        primary = airSensor.getDesc();
                        data = airAvgToHourModel.getCoAvg();
                    }
                    break;
                case O3:
                    int o3_1h = AQIComputedUtil.o3_1h(airAvgToHourModel.getO3Avg());
                    // 分指数
                    aqiHour.setO3Index(o3_1h);
                    if (o3_1h > maxIAQI) {
                        maxIAQI = o3_1h;
                        primary = airSensor.getDesc();
                        data = airAvgToHourModel.getO3Avg();
                    }
                    break;
            }
        }
        // 计算空气质量等级
        for (AqiStandard aqiStandard : AqiStandard.values()) {
            if (maxIAQI >= aqiStandard.getMin() && maxIAQI <= aqiStandard.getMax()) {
                aqiHour.setQuality(aqiStandard.getQuality());
                aqiHour.setLevel(aqiStandard.getLevel());
                aqiHour.setTips(aqiStandard.getTips());
            }
        }
        // IAQI值是否大于50
        if (maxIAQI <= 50) {
            primary = "-";
            data = null;
        }
        aqiHour.setDeviceNo(deviceNo);
        aqiHour.setDateTime(dateTime);
        aqiHour.setAqi(maxIAQI);
        aqiHour.setPollute(primary);
        aqiHour.setData(data);
        aqiHour.setPm25(airAvgToHourModel.getPm25Avg());
        aqiHour.setPm10(airAvgToHourModel.getPm10Avg());
        aqiHour.setNo2(airAvgToHourModel.getNo2Avg());
        aqiHour.setSo2(airAvgToHourModel.getSo2Avg());
        aqiHour.setCo(airAvgToHourModel.getCoAvg());
        aqiHour.setO3(airAvgToHourModel.getO3Avg());
        aqiHour.setVoc(airAvgToHourModel.getVocAvg());
        // 返回
        return aqiHour;
    }

    /**
     * 计算AQI日值
     *
     * @param deviceNo         设备号
     * @param dateTime         接收时间
     * @param airAvgToDayModel 空气站日平均值
     * @return 信息
     */
    public static AqiDay computedAqiToDay(String deviceNo, LocalDateTime dateTime, AirAvgModel airAvgToDayModel) {
        // AQI日浓度值实体
        AqiDay aqiDay = new AqiDay();
        int maxIAQI = -1; // 最大IAQI，即是AQI值
        String primary = "-"; // 首要污染物
        BigDecimal data = new BigDecimal(0); // 首要污染物浓度值
        // 计算最大AQI和首要污染物值
        for (AirSensor airSensor : AirSensor.values()) {
            switch (airSensor) {
                case PM25:
                    int pm25_24h = AQIComputedUtil.pm25_24h(airAvgToDayModel.getPm25Avg());
                    // 分指数
                    aqiDay.setPm25Index(pm25_24h);
                    if (pm25_24h > maxIAQI) {
                        maxIAQI = pm25_24h;
                        primary = airSensor.getDesc();
                        data = airAvgToDayModel.getPm25Avg();
                    }
                    break;
                case PM10:
                    int pm10_24h = AQIComputedUtil.pm10_24h(airAvgToDayModel.getPm10Avg());
                    // 分指数
                    aqiDay.setPm10Index(pm10_24h);
                    if (pm10_24h > maxIAQI) {
                        maxIAQI = pm10_24h;
                        primary = airSensor.getDesc();
                        data = airAvgToDayModel.getPm10Avg();
                    }
                    break;
                case SO2:
                    int so2_24h = AQIComputedUtil.so2_24h(airAvgToDayModel.getSo2Avg());
                    // 分指数
                    aqiDay.setSo2Index(so2_24h);
                    if (so2_24h > maxIAQI) {
                        maxIAQI = so2_24h;
                        primary = airSensor.getDesc();
                        data = airAvgToDayModel.getSo2Avg();
                    }
                    break;
                case NO2:
                    int no2_24h = AQIComputedUtil.no2_24h(airAvgToDayModel.getNo2Avg());
                    // 分指数
                    aqiDay.setNo2Index(no2_24h);
                    if (no2_24h > maxIAQI) {
                        maxIAQI = no2_24h;
                        primary = airSensor.getDesc();
                        data = airAvgToDayModel.getNo2Avg();
                    }
                    break;
                case CO:
                    int co_24h = AQIComputedUtil.co_24h(airAvgToDayModel.getCoAvg());
                    // 分指数
                    aqiDay.setCoIndex(co_24h);
                    if (co_24h > maxIAQI) {
                        maxIAQI = co_24h;
                        primary = airSensor.getDesc();
                        data = airAvgToDayModel.getCoAvg();
                    }
                    break;
                case O3:
                    int o3_8h = AQIComputedUtil.o3_8h(airAvgToDayModel.getO3Avg());
                    // 分指数
                    aqiDay.setO3Index(o3_8h);
                    if (o3_8h > maxIAQI) {
                        maxIAQI = o3_8h;
                        primary = airSensor.getDesc();
                        data = airAvgToDayModel.getO3Avg();
                    }
                    break;
            }
        }
        // 计算空气质量等级
        for (AqiStandard aqiStandard : AqiStandard.values()) {
            if (maxIAQI >= aqiStandard.getMin() && maxIAQI <= aqiStandard.getMax()) {
                aqiDay.setQuality(aqiStandard.getQuality());
                aqiDay.setLevel(aqiStandard.getLevel());
                aqiDay.setTips(aqiStandard.getTips());
            }
        }
        // IAQI值是否大于50
        if (maxIAQI <= 50) {
            primary = "-";
            data = null;
        }
        aqiDay.setDeviceNo(deviceNo);
        aqiDay.setDateTime(dateTime);
        aqiDay.setAqi(maxIAQI);
        aqiDay.setPollute(primary);
        aqiDay.setData(data);
        aqiDay.setPm25(airAvgToDayModel.getPm25Avg());
        aqiDay.setPm10(airAvgToDayModel.getPm10Avg());
        aqiDay.setNo2(airAvgToDayModel.getNo2Avg());
        aqiDay.setSo2(airAvgToDayModel.getSo2Avg());
        aqiDay.setCo(airAvgToDayModel.getCoAvg());
        aqiDay.setO3(airAvgToDayModel.getO3Avg());
        aqiDay.setVoc(airAvgToDayModel.getVocAvg());
        // 返回
        return aqiDay;
    }
}