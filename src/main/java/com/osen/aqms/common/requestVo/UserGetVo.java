package com.osen.aqms.common.requestVo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * User: PangYi
 * Date: 2019-12-24
 * Time: 16:57
 * Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserGetVo {

    private String number;

    private String search;
}
