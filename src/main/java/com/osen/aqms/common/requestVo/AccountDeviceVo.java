package com.osen.aqms.common.requestVo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * User: PangYi
 * Date: 2019-12-25
 * Time: 9:38
 * Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountDeviceVo {

    private String account;

    private String deviceNo;
}
