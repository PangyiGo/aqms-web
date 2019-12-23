package com.osen.aqms.common.requestVo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * User: PangYi
 * Date: 2019-12-23
 * Time: 14:28
 * Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AirMonitorVo {

    private String address;

    private String level;

    private String time;
}
