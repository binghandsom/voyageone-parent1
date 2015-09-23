package com.voyageone.batch.synship.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.batch.synship.modelbean.TrackingBean;
import com.voyageone.batch.synship.modelbean.TrackingSyncBean;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ReservationDao extends BaseDao {

    @Override
    protected String namespace() {
        return "com.voyageone.batch.synship.sql";
    }

    /**
     *
     * @param syn_ship_no
     * @param status
     * @param task_name
     * @return
     */
    public int UpdateReservationStatus(String syn_ship_no, String status, String task_name) {

        Map<String, Object> params = new HashMap<>();

        params.put("syn_ship_no", syn_ship_no);
        params.put("status", status);
        params.put("task_name", task_name);

        return updateTemplate.insert( "synShip_UpdateReservationStatus", params);
    }

    /**
     *
     * @param syn_ship_no
     * @param res_note
     * @param task_name
     * @return
     */
    public int insertReservationLog(String syn_ship_no, String res_note, String task_name) {

        Map<String, Object> params = new HashMap<>();

        params.put("syn_ship_no", syn_ship_no);
        params.put("res_note", res_note);
        params.put("task_name", task_name);

        return updateTemplate.insert( "synShip_InsertReservationLog", params);
    }


}
