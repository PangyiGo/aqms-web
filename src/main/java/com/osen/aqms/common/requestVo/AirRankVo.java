package com.osen.aqms.common.requestVo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * User: PangYi
 * Date: 2019-12-19
 * Time: 9:30
 * Description: 空气质量参数排名请求体参数
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AirRankVo {

    private String address;

    private String level;

    private String time;
}
