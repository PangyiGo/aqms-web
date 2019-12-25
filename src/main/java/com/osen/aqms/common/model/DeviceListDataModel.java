package com.osen.aqms.common.model;

import com.osen.aqms.modules.entity.system.Device;
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
public class DeviceListDataModel {

    private long total;

    private List<Device> userList = new ArrayList<>(0);
}
