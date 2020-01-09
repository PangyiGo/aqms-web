package com.osen.aqms.modules.entity.address;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * User: PangYi
 * Date: 2019-11-29
 * Time: 16:10
 * Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("web_address")
public class WebAddress extends Model<WebAddress> implements Serializable {

    // 主键
    @TableId(type = IdType.INPUT)
    private Integer id;

    // 省市区名称
    private String name;

    // 上级ID
    private int parentid;

    // 简称
    private String shortname;

    // 级别:0,中国；1，省分；2，市；3，区、县
    private int leveltype;

    // 城市代码
    private String webAddresscode;

    // 邮编
    private String zipcode;

    // 经度
    private String lng;

    // 纬度
    private String lat;

    // 拼音
    private String pinyin;

    private int status;
}
