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
 * Time: 14:47
 * Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeviceStatusModel {

    private int online;

    private int offline;

    private long total;

    private List<Device> deviceList = new ArrayList<>(0);
}
