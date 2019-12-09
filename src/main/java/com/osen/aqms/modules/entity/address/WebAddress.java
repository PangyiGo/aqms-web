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

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String code;

    private String name;

    private int level;

    private String fullName;

    private String longitude;

    private String latitude;

    private String parentId;

    private String pinyin;
}
