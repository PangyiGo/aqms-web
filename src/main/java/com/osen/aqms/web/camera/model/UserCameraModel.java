package com.osen.aqms.web.camera.model;

import lombok.Data;

import java.io.Serializable;

/**
 * User: PangYi
 * Date: 2020-02-24
 * Time: 10:37
 * Description:
 */
@Data
public class UserCameraModel implements Serializable {

    private String deviceNo;

    private String deviceName;

    private String province;

    private String city;

    private String area;

    private String address;

    private String remark;

    private Integer live;

    private String serial;

}
