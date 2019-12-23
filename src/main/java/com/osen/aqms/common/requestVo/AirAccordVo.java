package com.osen.aqms.common.requestVo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * User: PangYi
 * Date: 2019-12-23
 * Time: 10:01
 * Description:
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class AirAccordVo {

    private String address;

    private String level;

    private String time;

    private String type;
}
