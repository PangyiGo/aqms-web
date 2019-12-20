package com.osen.aqms.common.requestVo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * User: PangYi
 * Date: 2019-12-20
 * Time: 9:55
 * Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AqiReportVo {

    private String address;

    private String level;

    private String time;

    private String hour;
}
