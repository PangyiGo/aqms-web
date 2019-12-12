package com.osen.aqms.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * User: PangYi
 * Date: 2019-12-11
 * Time: 9:21
 * Description: 设备树形列表
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeviceTreeModel {

    private String label;

    private String code;

    private String deviceNo;

    private List<DeviceTreeModel> children = new ArrayList<>(0);

}