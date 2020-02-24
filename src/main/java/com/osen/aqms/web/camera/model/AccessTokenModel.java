package com.osen.aqms.web.camera.model;

import lombok.Data;

import java.io.Serializable;

/**
 * User: PangYi
 * Date: 2020-02-24
 * Time: 9:07
 * Description:
 */
@Data
public class AccessTokenModel implements Serializable {

    private String code;

    private String msg;

    private DataModel data = new DataModel();

}