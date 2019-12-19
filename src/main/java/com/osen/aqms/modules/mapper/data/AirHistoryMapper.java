package com.osen.aqms.modules.mapper.data;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.osen.aqms.common.model.AirAvgModel;
import com.osen.aqms.modules.entity.data.AirHistory;
import io.lettuce.core.dynamic.annotation.Param;

import java.time.LocalDateTime;

/**
 * User: PangYi
 * Date: 2019-11-30
 * Time: 9:02
 * Description:
 */
public interface AirHistoryMapper extends BaseMapper<AirHistory> {

    /**
     * 获取每日的空气站参数浓度值
     *
     * @param tableName 表名
     * @param deviceNo  设备号
     * @param start     开始时间
     * @param end       结束时间
     * @return 信息
     */
    AirAvgModel getAvgToDay(@Param("tableName") String tableName, @Param("deviceNo") String deviceNo, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}
