package com.osen.aqms.common.model;

import com.osen.aqms.modules.entity.system.Device;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * User: PangYi
 * Date: 2020-01-09
 * Time: 10:32
 * Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeviceNumberModel {

    private int allDevice;

    private int onlineDevice;

    private int offlineDevice;

    private int alarmDevice;

    private List<Device> onlineDevices = new ArrayList<>(0);

    private List<Device> offlineDevices = new ArrayList<>(0);

    private List<Device> alarmDevices = new ArrayList<>(0);
}
