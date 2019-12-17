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
 * Time: 14:33
 * Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("air_history")
public class AirHistory extends Model<AirHistory> implements Serializable {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String deviceNo;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateTime;

    private BigDecimal pm25;

    private BigDecimal pm10;

    private BigDecimal so2;

    private BigDecimal no2;

    private BigDecimal co;

    private BigDecimal o3;

    private BigDecimal voc;

    private String pm25Flag;

    private String pm10Flag;

    private String so2Flag;

    private String no2Flag;

    private String coFlag;

    private String o3Flag;

    private String vocFlag;

}
