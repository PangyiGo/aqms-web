package com.osen.aqms.common.requestVo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * User: PangYi
 * Date: 2019-12-24
 * Time: 17:51
 * Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserModifyVo {

    private Integer id;

    private String account;

    private String customer = "";

    private String phone = "";

    private String company = "";

    private String address = "";

    private String remark = "";

    private String roleName;
}
