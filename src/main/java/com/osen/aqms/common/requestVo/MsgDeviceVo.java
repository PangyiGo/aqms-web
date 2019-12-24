package com.osen.aqms.common.requestVo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * User: PangYi
 * Date: 2019-12-24
 * Time: 16:02
 * Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MsgDeviceVo {

    private String address;

    private String level;

    private String startTime;

    private String endTime;
}
