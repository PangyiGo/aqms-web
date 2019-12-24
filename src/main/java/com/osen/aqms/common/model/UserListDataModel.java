package com.osen.aqms.common.model;

import com.osen.aqms.modules.entity.system.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * User: PangYi
 * Date: 2019-12-24
 * Time: 17:14
 * Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserListDataModel {

    private long total;

    private List<User> userList = new ArrayList<>(0);
}
