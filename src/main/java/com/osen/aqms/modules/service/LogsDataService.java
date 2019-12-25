package com.osen.aqms.modules.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.osen.aqms.common.requestVo.LogsVo;
import com.osen.aqms.modules.entity.logs.LogsData;

import java.util.Map;

/**
 * User: PangYi
 * Date: 2019-11-30
 * Time: 9:24
 * Description:
 */
public interface LogsDataService extends IService<LogsData> {


    Map<String,Object> getLogsDataHistory(LogsVo logsVo);
}
