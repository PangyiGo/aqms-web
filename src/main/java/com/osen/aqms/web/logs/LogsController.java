package com.osen.aqms.web.logs;

import com.osen.aqms.common.requestVo.LogsVo;
import com.osen.aqms.common.result.RestResult;
import com.osen.aqms.common.utils.RestResultUtil;
import com.osen.aqms.modules.service.LogsDataService;
import com.osen.aqms.modules.service.LogsLoginService;
import com.osen.aqms.modules.service.LogsOpsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * User: PangYi
 * Date: 2019-12-25
 * Time: 16:27
 * Description:
 */
@RestController
@RequestMapping("${restful.prefix}")
@Slf4j
public class LogsController {

    @Autowired
    private LogsLoginService logsLoginService;

    @Autowired
    private LogsDataService logsDataService;

    @Autowired
    private LogsOpsService logsOpsService;

    @PostMapping("/logs/login/history")
    public RestResult getLogsLoginHistory(@RequestBody LogsVo logsVo) {
        Map<String, Object> logsLoginHistory = logsLoginService.getLogsLoginHistory(logsVo);
        return RestResultUtil.success(logsLoginHistory);
    }

    @PostMapping("/logs/data/history")
    public RestResult getLogsDataHistory(@RequestBody LogsVo logsVo) {
        Map<String, Object> logsDataHistory = logsDataService.getLogsDataHistory(logsVo);
        return RestResultUtil.success(logsDataHistory);
    }

    @PostMapping("/logs/ops/history")
    public RestResult getLogsOpsHistory(@RequestBody LogsVo logsVo) {
        Map<String, Object> logsOpsHistory = logsOpsService.getLogsOpsHistory(logsVo);
        return RestResultUtil.success(logsOpsHistory);
    }
}
