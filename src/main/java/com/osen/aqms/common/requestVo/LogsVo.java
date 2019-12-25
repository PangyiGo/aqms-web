package com.osen.aqms.common.requestVo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * User: PangYi
 * Date: 2019-12-25
 * Time: 16:08
 * Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogsVo {

    private String number;

    private String startTime;

    private String endTime;
}
