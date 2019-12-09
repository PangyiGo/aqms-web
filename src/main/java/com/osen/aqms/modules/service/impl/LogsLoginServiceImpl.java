package com.osen.aqms.modules.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.osen.aqms.modules.entity.logs.LogsLogin;
import com.osen.aqms.modules.mapper.LogsLoginMapper;
import com.osen.aqms.modules.service.LogsLoginService;
import org.springframework.stereotype.Service;

/**
 * User: PangYi
 * Date: 2019-11-30
 * Time: 10:52
 * Description:
 */
@Service
public class LogsLoginServiceImpl extends ServiceImpl<LogsLoginMapper, LogsLogin> implements LogsLoginService {
}
