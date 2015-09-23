package com.voyageone.wms.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.common.Constants;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ReservationLogDao extends BaseDao {

     /**
     * 插入ReservationLog
     * @param reservationList 捡货一览
     * @param resNote 备注
     * @param userName 更新者
     * @return int
     */
    public int insertReservationLog(List<Long> reservationList, String resNote, String userName) {

        Map<String, Object> params = new HashMap<>();

        params.put("reservationList", reservationList);
        params.put("resNote", resNote);
        params.put("userName", userName);

        return updateTemplate.insert(Constants.DAO_NAME_SPACE_WMS + "wms_bt_reservation_log_insert", params);
    }

}
