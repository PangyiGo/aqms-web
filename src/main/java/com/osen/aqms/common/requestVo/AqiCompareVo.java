package com.osen.aqms.common.requestVo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * User: PangYi
 * Date: 2019-12-24
 * Time: 10:59
 * Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AqiCompareVo {

    private String dev1;

    private String dev2;

    private String startTime;

    private String endTime;
}
