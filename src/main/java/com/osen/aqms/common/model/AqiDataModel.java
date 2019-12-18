package com.osen.aqms.common.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AqiDataModel {

    @JsonFormat(pattern = "yyyy-MM-dd HH时")
    private LocalDateTime dateTime;

    private int data = 0;
}