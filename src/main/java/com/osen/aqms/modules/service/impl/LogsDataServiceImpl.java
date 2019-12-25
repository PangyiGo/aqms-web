package com.osen.aqms.modules.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.osen.aqms.common.requestVo.LogsVo;
import com.osen.aqms.common.utils.ConstUtil;
import com.osen.aqms.common.utils.DateTimeUtil;
import com.osen.aqms.modules.entity.logs.LogsData;
import com.osen.aqms.modules.mapper.logs.LogsDataMapper;
import com.osen.aqms.modules.service.LogsDataService;
import org.springframework.stereotype.Service;

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
public class LogsDataServiceImpl extends ServiceImpl<LogsDataMapper, LogsData> implements LogsDataService {

    @Override
    public Map<String, Object> getLogsDataHistory(LogsVo logsVo) {
        Map<String, Object> map = new HashMap<>(0);
        long total = 0;
        List<LogsData> logsData = new ArrayList<>(0);
        LocalDate localDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(ConstUtil.QUERY_DATE);
        String startTime = logsVo.getStartTime();
        String endTime = logsVo.getEndTime();
        if (startTime == null || "".equals(startTime.trim()))
            startTime = localDate.format(formatter);
        if (endTime == null || "".equals(endTime.trim()))
            endTime = localDate.format(formatter);
        List<LocalDateTime> dateTimes = DateTimeUtil.queryTimeFormatter(startTime, endTime);
        LambdaQueryWrapper<LogsData> wrapper = Wrappers.<LogsData>lambdaQuery().between(LogsData::getCreateTime, dateTimes.get(0), dateTimes.get(1));
        Page<LogsData> loginPage = new Page<>(Integer.valueOf(logsVo.getNumber()), ConstUtil.PAGENUMBER);
        IPage<LogsData> loginIPage = super.page(loginPage, wrapper);
        if (loginIPage.getTotal() > 0) {
            total = loginIPage.getTotal();
            logsData = loginPage.getRecords();
        }
        map.put("total", total);
        map.put("logs", logsData);
        return map;
    }
}
