package com.osen.aqms.modules.entity.data;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * User: PangYi
 * Date: 2019-11-29
 * Time: 14:38
 * Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("air_minute")
public class AirMinute extends Model<AirMinute> implements Serializable {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String deviceNo;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateTime;

    private BigDecimal pm25;

    private BigDecimal pm25Max;

    private BigDecimal pm25Min;

    private BigDecimal pm10;

    private BigDecimal pm10Max;

    private BigDecimal pm10Min;

    private BigDecimal so2;

    private BigDecimal so2Max;

    private BigDecimal so2Min;

    private BigDecimal no2;

    private BigDecimal no2Max;

    private BigDecimal no2Min;

    private BigDecimal co;

    private BigDecimal coMax;

    private BigDecimal coMin;

    private BigDecimal o3;

    private BigDecimal o3Max;

    private BigDecimal o3Min;

    private BigDecimal voc;

    private BigDecimal vocMax;

    private BigDecimal vocMin;

    private String pm25Flag;

    private String pm10Flag;

    private String so2Flag;

    private String no2Flag;

    private String coFlag;

    private String o3Flag;

    private String vocFlag;
}
