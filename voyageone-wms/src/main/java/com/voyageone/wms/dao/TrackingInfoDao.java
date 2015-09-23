package com.voyageone.wms.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.common.Constants;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Jack on 5/29/2015.
 */
@Repository
public class TrackingInfoDao extends BaseDao {

     /**
     * 插入物流信息
     * @param reservationList 捡货一览
     * @param trackingStatus 物品的物流状态
     * @param userName 更新者
     * @return int
     */
    public int insertTrackingInfo(List<Long> reservationList, String trackingStatus,  String userName) {

        Map<String, Object> params = new HashMap<>();

        params.put("reservationList", reservationList);
        params.put("trackingStatus", trackingStatus);
        params.put("userName", userName);

        return updateTemplate.insert(Constants.DAO_NAME_SPACE_WMS + "tt_tracking_info_insert", params);
    }

}
