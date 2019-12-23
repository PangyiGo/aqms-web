package com.osen.aqms.modules.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.osen.aqms.common.model.AirQueryDataModel;
import com.osen.aqms.common.requestVo.AirExportVo;
import com.osen.aqms.common.requestVo.AirQueryVo;
import com.osen.aqms.modules.entity.data.AirMinute;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.util.List;

/**
 * User: PangYi
 * Date: 2019-11-30
 * Time: 10:47
 * Description:
 */
public interface AirMinuteService extends IService<AirMinute> {

    /**
     * 查询设备的空气空气参数分钟历史数据
     *
     * @param airQueryVo 请求体
     * @return 信息
     */
    List<AirQueryDataModel> getAirMinuteHistory(AirQueryVo airQueryVo);

    /**
     * 空气站分钟数据报表导出
     *
     * @param airExportVo 请求体
     * @return 信息
     */
    HSSFWorkbook getAirSensorToExport(AirExportVo airExportVo);
}
