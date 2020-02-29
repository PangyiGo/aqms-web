package com.osen.aqms.web.data_air.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.osen.aqms.common.model.AirQueryDataModel;
import com.osen.aqms.common.model.AqiDataToMapModel;
import com.osen.aqms.common.model.AqiRealtimeModel;
import com.osen.aqms.common.utils.ConstUtil;
import com.osen.aqms.common.utils.RedisOpsUtil;
import com.osen.aqms.common.utils.TableNameUtil;
import com.osen.aqms.modules.entity.system.Device;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * User: PangYi
 * Date: 2019-12-23
 * Time: 16:30
 * Description:
 */
@Component
@Slf4j
public class AirSensorHistoryExport {

    @Autowired
    private RedisOpsUtil redisOpsUtil;

    /**
     * 导出空气站实时数据报表
     *
     * @param listMap 数据
     * @return 信息
     */
    public HSSFWorkbook exportToAirHistory(Map<String, List<AirQueryDataModel>> listMap) {
        Set<String> sheetName = listMap.keySet();
        // 创建Excel工作簿
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
        try {
            for (String name : sheetName) {
                // 创建工作表
                HSSFSheet sheet = hssfWorkbook.createSheet(name);

                // 表头
                HSSFRow head = sheet.createRow(0);
                head.createCell(0).setCellValue("数据日期");
                head.createCell(1).setCellValue("PM2.5(μg/m³)");
                head.createCell(2).setCellValue("PM10(μg/m³)");
                head.createCell(3).setCellValue("SO2(μg/m³)");
                head.createCell(4).setCellValue("NO2(μg/m³)");
                head.createCell(5).setCellValue("CO(mg/m³)");
                head.createCell(6).setCellValue("O3(μg/m³)");
                head.createCell(7).setCellValue("TVOC(mg/m³)");

                // 导出的数据
                List<AirQueryDataModel> history = listMap.get(name);
                int rowIndex = 1;
                for (AirQueryDataModel airQueryDataModel : history) {
                    // 创建新行
                    HSSFRow dataRow = sheet.createRow(rowIndex++);

                    // 时间
                    HSSFCell time = dataRow.createCell(0, CellType.STRING);
                    DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    time.setCellValue(df.format(airQueryDataModel.getDateTime()));

                    // PM2.5
                    HSSFCell pm25 = dataRow.createCell(1, CellType.STRING);
                    pm25.setCellValue(airQueryDataModel.getPm25() == null ? "" : airQueryDataModel.getPm25().toString());

                    // PM10
                    HSSFCell pm10 = dataRow.createCell(2, CellType.STRING);
                    pm10.setCellValue(airQueryDataModel.getPm10() == null ? "" : airQueryDataModel.getPm10().toString());

                    // SO2
                    HSSFCell so2 = dataRow.createCell(3, CellType.STRING);
                    so2.setCellValue(airQueryDataModel.getSo2() == null ? "" : airQueryDataModel.getSo2().toString());

                    // NO2
                    HSSFCell no2 = dataRow.createCell(4, CellType.STRING);
                    no2.setCellValue(airQueryDataModel.getNo2() == null ? "" : airQueryDataModel.getNo2().toString());

                    // CO
                    HSSFCell co = dataRow.createCell(5, CellType.STRING);
                    co.setCellValue(airQueryDataModel.getCo() == null ? "" : airQueryDataModel.getCo().toString());

                    // O3
                    HSSFCell o3 = dataRow.createCell(6, CellType.STRING);
                    o3.setCellValue(airQueryDataModel.getO3() == null ? "" : airQueryDataModel.getO3().toString());

                    // TVOC
                    HSSFCell voc = dataRow.createCell(7, CellType.STRING);
                    voc.setCellValue(airQueryDataModel.getVoc() == null ? "" : airQueryDataModel.getVoc().toString());
                }

                // 设置默认表格列宽
                sheet.setDefaultColumnWidth(80);
                sheet.setDefaultRowHeight((short) (19 * 23));
                //列宽自适应
                for (int a = 0; a <= 8; a++) {
                    sheet.autoSizeColumn(a);
                }
            }
            // 返回
            return hssfWorkbook;
        } catch (Exception e) {
            log.error("export excel exception {}", e.getMessage());
            return null;
        }
    }

    /**
     * 数据封装
     *
     * @param device 设备
     * @return 信息
     */
    public AqiDataToMapModel getAqiDataModel(Device device) {
        AqiDataToMapModel aqiDataToMapModel = new AqiDataToMapModel();
        aqiDataToMapModel.setDeviceName(device.getDeviceName());
        aqiDataToMapModel.setDeviceNo(device.getDeviceNo());
        String ade = (StrUtil.isNotEmpty(device.getProvince()) ? device.getProvince() : "") + (StrUtil.isNotEmpty(device.getCity()) ? device.getCity() : "") + (StrUtil.isNotEmpty(device.getArea()) ? device.getArea() : "");
        aqiDataToMapModel.setAddress(ade);
        aqiDataToMapModel.setInstallAddress((StrUtil.isNotEmpty(device.getAddress()) ? device.getAddress() : ""));
        aqiDataToMapModel.setLive(ConstUtil.OPEN_STATUS.equals(device.getLive()) ? "在线" : "离线");
        aqiDataToMapModel.setLongitude(device.getLongitude());
        aqiDataToMapModel.setLatitude(device.getLatitude());
        // 获取实时数据
        String dataJson = redisOpsUtil.getToMap(TableNameUtil.Air_Realtime, device.getDeviceNo());
        if (dataJson != null) {
            AqiRealtimeModel aqiRealtimeModel = JSON.parseObject(dataJson, AqiRealtimeModel.class);
            BeanUtil.copyProperties(aqiRealtimeModel, aqiDataToMapModel);
        }
        return aqiDataToMapModel;
    }
}
