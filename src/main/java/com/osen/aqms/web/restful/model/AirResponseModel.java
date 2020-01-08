package com.osen.aqms.web.restful.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AirResponseModel {

    private Integer code;

    private String msg;
}
