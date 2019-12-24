package com.osen.aqms.common.model;

import com.osen.aqms.modules.entity.alarm.AlarmControl;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * User: PangYi
 * Date: 2019-12-24
 * Time: 15:11
 * Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlarmControlModel {

    private AirQueryDataModel airQueryDataModel;

    private AlarmControl alarmControl;
}
