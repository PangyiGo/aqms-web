package com.osen.aqms.modules.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.osen.aqms.common.requestVo.LogsVo;
import com.osen.aqms.common.utils.ConstUtil;
import com.osen.aqms.common.utils.DateTimeUtil;
import com.osen.aqms.common.utils.IpUtil;
import com.osen.aqms.common.utils.SecurityUtil;
import com.osen.aqms.modules.entity.logs.LogsLogin;
import com.osen.aqms.modules.mapper.logs.LogsLoginMapper;
import com.osen.aqms.modules.service.LogsLoginService;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: PangYi
 * Date: 2019-11-30
 * Time: 10:52
 * Description:
 */
@Service
public class LogsLoginServiceImpl extends ServiceImpl<LogsLoginMapper, LogsLogin> implements LogsLoginService {

    @Override
    public void saveLogs(HttpServletRequest request, String message) {
        String username = SecurityUtil.getUsername();
        LogsLogin logsLogin = new LogsLogin();
        // IP
        String ipAddr = IpUtil.getIpAddr(request);
        logsLogin.setAccount(username == null ? "" : username);
        logsLogin.setIp(ipAddr);
        logsLogin.setOps(message);
        logsLogin.setCreateTime(LocalDateTime.now());

        super.save(logsLogin);
    }

    @Override
    public Map<String, Object> getLogsLoginHistory(LogsVo logsVo) {
        Map<String, Object> map = new HashMap<>(0);
        long total = 0;
        List<LogsLogin> logsLogins = new ArrayList<>(0);
        LocalDate localDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(ConstUtil.QUERY_DATE);
        String startTime = logsVo.getStartTime();
        String endTime = logsVo.getEndTime();
        if (startTime == null || "".equals(startTime.trim()))
            startTime = localDate.format(formatter);
        if (endTime == null || "".equals(endTime.trim()))
            endTime = localDate.format(formatter);
        // 用户名
        String username = SecurityUtil.getUsername();
        List<LocalDateTime> dateTimes = DateTimeUtil.queryTimeFormatter(startTime, endTime);
        LambdaQueryWrapper<LogsLogin> wrapper = Wrappers.<LogsLogin>lambdaQuery().eq(LogsLogin::getAccount, username).between(LogsLogin::getCreateTime, dateTimes.get(0), dateTimes.get(1));
        Page<LogsLogin> loginPage = new Page<>(Integer.valueOf(logsVo.getNumber()), ConstUtil.PAGENUMBER);
        IPage<LogsLogin> loginIPage = super.page(loginPage, wrapper);
        if (loginIPage.getTotal() > 0) {
            total = loginIPage.getTotal();
            logsLogins = loginPage.getRecords();
        }
        map.put("total", total);
        map.put("logs", logsLogins);
        return map;
    }
}
