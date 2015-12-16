package com.voyageone.batch.oms.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.common.Constants;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ReservationDao extends BaseDao {


    /**
     * 更新Reservation的状态
     * @param orderNumber 订单号
     * @param reservationList reservationID一览
     * @param status 状态
     * @return long
     */
    public long updateReservationStatus(long orderNumber, List<Long> reservationList, String status, String takeName) {

        Map<String, Object> params = new HashMap<>();

        params.put("orderNumber", orderNumber);
        params.put("reservationList", reservationList);
        params.put("status", status);
        params.put("takeName", takeName);

        return updateTemplate.update(Constants.DAO_NAME_SPACE_OMS + "wms_updateReservationStatus", params);

    }

    /**
     * 插入ReservationLog
     * @param reservationList reservation一览
     * @param resNote 备注
     * @param takeName 更新者
     * @return int
     */
    public int insertReservationLog(List<Long> reservationList, String resNote, String takeName) {

        Map<String, Object> params = new HashMap<>();

        params.put("reservationList", reservationList);
        params.put("resNote", resNote);
        params.put("takeName", takeName);

        return updateTemplate.insert(Constants.DAO_NAME_SPACE_OMS + "wms_bt_reservation_log_insert", params);
    }


}
