package com.osen.aqms.web.data_air;

import com.osen.aqms.common.exception.type.ControllerException;
import com.osen.aqms.common.model.AirQueryDataModel;
import com.osen.aqms.common.requestVo.AirExportVo;
import com.osen.aqms.common.requestVo.AirQueryVo;
import com.osen.aqms.common.result.RestResult;
import com.osen.aqms.common.utils.ConstUtil;
import com.osen.aqms.common.utils.RestResultUtil;
import com.osen.aqms.modules.service.AirDayService;
import com.osen.aqms.modules.service.AirHistoryService;
import com.osen.aqms.modules.service.AirHourService;
import com.osen.aqms.modules.service.AirMinuteService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;

/**
 * User: PangYi
 * Date: 2019-12-19
 * Time: 13:54
 * Description: 空气参数历史数据查询
 */
@RestController
@RequestMapping("${restful.prefix}")
@Slf4j
public class AirDataHistoryController {

    @Autowired
    private AirHistoryService airHistoryService;

    @Autowired
    private AirMinuteService airMinuteService;

    @Autowired
    private AirHourService airHourService;

    @Autowired
    private AirDayService airDayService;

    /**
     * 根据设备号查询空气质量参数实时历史数据
     *
     * @param airQueryVo 请求体
     * @return 信息
     */
    @PostMapping("/airSensor/history")
    public RestResult getAirRealtimeHistory(@RequestBody AirQueryVo airQueryVo) {
        List<AirQueryDataModel> airRealtimeHistory = airHistoryService.getAirRealtimeHistory(airQueryVo);
        return RestResultUtil.success(airRealtimeHistory);
    }

    /**
     * 根据设备号查询空气质量参数分钟历史数据
     *
     * @param airQueryVo 请求体
     * @return 信息
     */
    @PostMapping("/airSensor/minute")
    public RestResult getAirMinuteHistory(@RequestBody AirQueryVo airQueryVo) {
        List<AirQueryDataModel> airMinuteHistory = airMinuteService.getAirMinuteHistory(airQueryVo);
        return RestResultUtil.success(airMinuteHistory);
    }

    /**
     * 根据设备号查询空气质量参数小时历史数据
     *
     * @param airQueryVo 请求体
     * @return 信息
     */
    @PostMapping("/airSensor/hour")
    public RestResult getAirHourHistory(@RequestBody AirQueryVo airQueryVo) {
        List<AirQueryDataModel> airHourHistory = airHourService.getAirHourHistory(airQueryVo);
        return RestResultUtil.success(airHourHistory);
    }

    /**
     * 根据设备号查询空气质量参数天历史数据
     *
     * @param airQueryVo 请求体
     * @return 信息
     */
    @PostMapping("/airSensor/day")
    public RestResult getAirDayHistory(@RequestBody AirQueryVo airQueryVo) {
        List<AirQueryDataModel> airDayHistory = airDayService.getAirDayHistory(airQueryVo);
        return RestResultUtil.success(airDayHistory);
    }

    /**
     * 空气站参数数据报表excel
     *
     * @param airExportVo 请求体
     * @param type        类型：实时(history)，分钟(minute)，小时(hour)，天(day)
     */
    @PostMapping("/airSensor/{type}")
    public void getAirSensorToExport(@RequestBody AirExportVo airExportVo, @PathVariable("type") String type, HttpServletResponse response) {
        HSSFWorkbook hssfWorkbook;
        switch (type) {
            case "history":
                hssfWorkbook = airHistoryService.getAirSensorToExport(airExportVo);
                if (hssfWorkbook == null)
                    throw new ControllerException("实时导出数据异常");
                break;
            case "minute":
                hssfWorkbook = airMinuteService.getAirSensorToExport(airExportVo);
                if (hssfWorkbook == null)
                    throw new ControllerException("分钟导出数据异常");
                break;
            case "hour":
                hssfWorkbook = airHourService.getAirSensorToExport(airExportVo);
                if (hssfWorkbook == null)
                    throw new ControllerException("小时导出数据异常");
                break;
            case "day":
                hssfWorkbook = airDayService.getAirSensorToExport(airExportVo);
                if (hssfWorkbook == null)
                    throw new ControllerException("天导出数据异常");
                break;
            default:
                throw new ControllerException("请求类型参数异常");
        }
        // Excel表格导出
        try {
            String title = "实时数据表格";
            String fileName = new String(title.getBytes("GB2312"), "ISO_8859_1");
            fileName = URLEncoder.encode(fileName, "utf-8");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xls");

            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setHeader("exportExcelCode", String.valueOf(ConstUtil.OK));

            OutputStream os = response.getOutputStream();
            hssfWorkbook.write(os);
            os.flush();
            os.close();
        } catch (Exception e) {
            log.error("导出实时数据Excel表格异常");
            log.error(e.getMessage());
        }
    }
}
