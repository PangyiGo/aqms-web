package com.osen.aqms.modules.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.osen.aqms.modules.entity.logs.LogsOps;
import com.osen.aqms.modules.mapper.logs.LogsOpsMapper;
import com.osen.aqms.modules.service.LogsOpsService;
import org.springframework.stereotype.Service;

/**
 * User: PangYi
 * Date: 2019-11-30
 * Time: 10:52
 * Description:
 */
@Service
public class LogsOpsServiceImpl extends ServiceImpl<LogsOpsMapper, LogsOps> implements LogsOpsService {
}
