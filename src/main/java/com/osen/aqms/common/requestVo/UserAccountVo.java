package com.osen.aqms.common.requestVo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * User: PangYi
 * Date: 2019-12-24
 * Time: 19:07
 * Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserAccountVo {

    private List<String> accounts = new ArrayList<>(0);
}
