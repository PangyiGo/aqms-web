package com.osen.aqms.common.enums;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * User: PangYi
 * Date: 2019-12-03
 * Time: 16:22
 * Description:
 */
@NoArgsConstructor
@AllArgsConstructor
public enum AqiStandard {

    OneLevel(1, "优", 0, 50, "可多参加户外活动，呼吸新鲜空气"),

    TwoLevel(2, "良", 51, 100, "除少数对某些污染物特别容易过敏的人群外，其他人群可以正常进行室外活动"),

    ThreeLevel(3, "轻度污染", 101, 150, "敏感人群需减少体力消耗较大的户外活动"),

    FourLevel(4, "中度污染", 151, 200, "敏感人群应尽量减少外出，一般人群适当减少户外运动"),

    FiveLevel(5, "重度污染", 201, 300, "敏感人群应停止户外运动，一般人群尽量减少户外运动"),

    SixLevel(6, "严重污染", 301, 1000, "除有特殊需要的人群外，尽量不要留在室外");

    private int level;

    private String quality;

    private int min;

    private int max;

    private String tips;

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }
}
