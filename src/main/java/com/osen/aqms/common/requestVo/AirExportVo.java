package com.osen.aqms.common.requestVo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * User: PangYi
 * Date: 2019-12-23
 * Time: 16:07
 * Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AirExportVo {

    private List<String> deviceNos = new ArrayList<>(0);

    private String startTime;

    private String endTime;
}
