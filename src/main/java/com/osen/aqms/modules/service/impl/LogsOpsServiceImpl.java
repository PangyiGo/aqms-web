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
import com.osen.aqms.modules.entity.logs.LogsOps;
import com.osen.aqms.modules.mapper.logs.LogsOpsMapper;
import com.osen.aqms.modules.service.LogsOpsService;
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
public class LogsOpsServiceImpl extends ServiceImpl<LogsOpsMapper, LogsOps> implements LogsOpsService {

    @Override
    public void addLogsOps(HttpServletRequest request, String message) {
        LogsOps logsOps = new LogsOps();
        String username = SecurityUtil.getUsername();
        // IP
        String ipAddr = IpUtil.getIpAddr(request);
        logsOps.setAccount(username);
        logsOps.setIp(ipAddr);
        logsOps.setOps(message);
        logsOps.setCreateTime(LocalDateTime.now());
        super.save(logsOps);
    }

    @Override
    public Map<String, Object> getLogsOpsHistory(LogsVo logsVo) {
        Map<String, Object> map = new HashMap<>(0);
        long total = 0;
        List<LogsOps> logsOpsList = new ArrayList<>(0);
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
        LambdaQueryWrapper<LogsOps> wrapper = Wrappers.<LogsOps>lambdaQuery().eq(LogsOps::getAccount, username).between(LogsOps::getCreateTime, dateTimes.get(0), dateTimes.get(1));
        Page<LogsOps> logsOpsPage = new Page<>(Integer.valueOf(logsVo.getNumber()), ConstUtil.PAGENUMBER);
        IPage<LogsOps> logsOpsIPage = super.page(logsOpsPage, wrapper);
        if (logsOpsIPage.getTotal() > 0) {
            total = logsOpsIPage.getTotal();
            logsOpsList = logsOpsIPage.getRecords();
        }
        map.put("total", total);
        map.put("logs", logsOpsList);
        return map;
    }
}
