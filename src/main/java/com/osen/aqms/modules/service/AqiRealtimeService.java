package com.osen.aqms.modules.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.osen.aqms.common.model.AqiDataToMapModel;
import com.osen.aqms.common.model.AqiRankMapModel;
import com.osen.aqms.common.model.AqiRealtimeMapModel;
import com.osen.aqms.modules.entity.data.AqiRealtime;

import java.util.List;

/**
 * User: PangYi
 * Date: 2019-11-30
 * Time: 10:47
 * Description:
 */
public interface AqiRealtimeService extends IService<AqiRealtime> {

    /**
     * 根据用户名获取用户所有设备列表并封装到地图模型实体
     *
     * @param username 用户名
     * @return 信息
     */
    List<AqiRealtimeMapModel> getAllMapToUsername(String username);

    /**
     * 根据设备号获取设备详细数据信息
     *
     * @param deviceNo 设备号
     * @return 信息
     */
    AqiDataToMapModel getAqiDataMapToDeviceNo(String deviceNo);

    /**
     * 根据区域，查询设备列表
     *
     * @param address 区域名称
     * @param level   区域等级
     * @return 信息
     */
    AqiRankMapModel getAqiRankToAddress(String address, String level);
}
