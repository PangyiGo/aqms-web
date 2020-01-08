package com.osen.aqms.web.restful.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AirResponseModel {

    // 请求成功编码
    public static Integer OK = 2002;

    // 请求失败编码
    public static Integer UNOK = 2004;

    private Integer code;

    private String msg;

    public static AirResponseModel success(String command) {
        AirResponseModel airResponseModel = new AirResponseModel();
        airResponseModel.setCode(OK);
        airResponseModel.setMsg("result command: " + command);
        return airResponseModel;
    }

    public static AirResponseModel fail(String error) {
        AirResponseModel airResponseModel = new AirResponseModel();
        airResponseModel.setCode(UNOK);
        airResponseModel.setMsg("error: " + error);
        return airResponseModel;
    }
}
