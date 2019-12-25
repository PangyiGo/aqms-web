package com.osen.aqms.modules.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.osen.aqms.common.requestVo.LogsVo;
import com.osen.aqms.modules.entity.logs.LogsLogin;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * User: PangYi
 * Date: 2019-11-30
 * Time: 9:24
 * Description:
 */
public interface LogsLoginService extends IService<LogsLogin> {

    void saveLogs(HttpServletRequest request, String message);

    Map<String,Object> getLogsLoginHistory(LogsVo logsVo);

}
