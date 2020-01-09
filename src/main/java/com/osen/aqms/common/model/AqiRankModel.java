package com.osen.aqms.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * User: PangYi
 * Date: 2020-01-09
 * Time: 11:37
 * Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AqiRankModel {

    private String deviceNo = "";

    private String deviceName = "";

    private int api;
}
