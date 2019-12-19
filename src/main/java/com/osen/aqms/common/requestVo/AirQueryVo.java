package com.osen.aqms.common.requestVo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * User: PangYi
 * Date: 2019-12-19
 * Time: 13:47
 * Description: 历史数据查询
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AirQueryVo {

    private String deviceNo;

    private String startTime;

    private String endTime;
}
