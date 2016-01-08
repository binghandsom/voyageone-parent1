package com.voyageone.batch.synship.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.batch.synship.modelbean.ReservationBean;
import com.voyageone.batch.synship.modelbean.ReservationClientBean;
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
    public int UpdateReservationStatus(String syn_ship_no, String status, String closeDayflg, String task_name) {

        Map<String, Object> params = new HashMap<>();

        params.put("syn_ship_no", syn_ship_no);
        params.put("status", status);
        params.put("close_day_flg", closeDayflg);
        params.put("task_name", task_name);

        return updateTemplate.update("synShip_UpdateReservationStatus", params);
    }

    /**
     *
     * @param syn_ship_no
     * @param status
     * @param before_status
     * @param task_name
     * @return
     */
    public int updateReservationByStatus(String syn_ship_no, String status, String before_status, String task_name) {

        Map<String, Object> params = new HashMap<>();

        params.put("syn_ship_no", syn_ship_no);
        params.put("status", status);
        params.put("before_status", before_status);
        params.put("task_name", task_name);

        return updateTemplate.update("synShip_UpdateReservationByStatus", params);
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

    /**
     *
     * @param syn_ship_no
     * @param res_note
     * @param status
     * @param task_name
     * @return
     */
    public int insertReservationLogByStatus(String syn_ship_no, String res_note, String status, String task_name) {

        Map<String, Object> params = new HashMap<>();

        params.put("syn_ship_no", syn_ship_no);
        params.put("res_note", res_note);
        params.put("status", status);
        params.put("task_name", task_name);

        return updateTemplate.insert( "synShip_InsertReservationLogByStatus", params);
    }

    /**
     * 取得发货渠道发生变化的订单
     * @param order_channel_id 订单渠道
     * @param status 状态
     * @return List<String>
     */
    public List<ReservationBean> getPreShipChannel(String order_channel_id, String status, List<String> exceptShipChannelList) {

        Map<String, Object> params = new HashMap<>();

        params.put("order_channel_id", order_channel_id);
        params.put("status", status);
        params.put("exceptShipChannelList", exceptShipChannelList);

        return selectList("synShip_selectPreShipChannel", params);
    }

    /**
     *
     * @param syn_ship_no
     * @param id
     * @param status
     * @param task_name
     * @return
     */
    public int updateReservationByShipChannel(String syn_ship_no, long id, String status, String ship_channel, String task_name) {

        Map<String, Object> params = new HashMap<>();

        params.put("syn_ship_no", syn_ship_no);
        params.put("id", id);
        params.put("status", status);
        params.put("ship_channel", ship_channel);
        params.put("task_name", task_name);

        return updateTemplate.update("synShip_UpdateReservationByShipChannel", params);
    }

    /**
     *
     * @param syn_ship_no
     * @param res_note
     * @param task_name
     * @return
     */
    public int insertReservationLogByID(String syn_ship_no, long id,  String res_note, String task_name) {

        Map<String, Object> params = new HashMap<>();

        params.put("syn_ship_no", syn_ship_no);
        params.put("id", id);
        params.put("res_note", res_note);
        params.put("task_name", task_name);

        return updateTemplate.insert( "synShip_InsertReservationLogByID", params);
    }


    /**
     * 取得相关渠道订单状态为Open的订单
     * @param order_channel_id 订单渠道
     * @param status 状态
     */
    public List<ReservationClientBean> getReservationDatas(String order_channel_id, String status) {

        Map<String, Object> params = new HashMap<>();

        params.put("order_channel_id", order_channel_id);
        params.put("status", status);

        return selectList("synShip_selectReservationDatas", params);
    }

    /**
     * 取得相关渠道店铺订单状态为Open的订单
     * @param order_channel_id 订单渠道
     * @param status 状态
     */
    public List<ReservationClientBean> getReservationDatasByShop(String order_channel_id, String cart_id, String status) {

        Map<String, Object> params = new HashMap<>();

        params.put("order_channel_id", order_channel_id);
        params.put("cart_id", cart_id);
        params.put("status", status);

        return selectList("synShip_selectReservationDatasByShop", params);
    }

    /**
     * 取得相关渠道店铺订单状态为Open的订单
     * @param order_channel_id 订单渠道
     * @param status 状态
     */
    public List<ReservationClientBean> getCancelReservationDatasByShop(String order_channel_id, String cart_id, String status) {

        Map<String, Object> params = new HashMap<>();

        params.put("order_channel_id", order_channel_id);
        params.put("cart_id", cart_id);
        params.put("status", status);

        return selectList("synShip_selectCancelReservationDatasByShop", params);
    }

    /**
     * 根据res_id和syn_ship_no更新res_status
     * @param syn_ship_no
     * @param status
     * @param task_name
     * @param longResids
     * @return
     */
    public int updateReservationBySynshipno(String syn_ship_no, String status,  String task_name,Long[] longResids) {

        Map<String, Object> params = new HashMap<>();
        params.put("syn_ship_no", syn_ship_no);
        params.put("status", status);
        params.put("task_name", task_name);
        //String resid = "(" + res_id + ")";
        params.put("longResids", longResids);
        return updateTemplate.update("synShip_UpdateReservationBySynshipno", params);
    }

    /**
     * 根据res_id和syn_ship_no插入wms_bt_reservation_log表
     * @param syn_ship_no
     * @param res_note
     * @param task_name
     * @param longResids
     * @return
     */
    public int insertReservationLogByInResID(String syn_ship_no, String res_note, String task_name,Long[] longResids) {

        Map<String, Object> params = new HashMap<>();

        params.put("syn_ship_no", syn_ship_no);
        params.put("res_note", res_note);
        params.put("task_name", task_name);
        //params.put("res_id", resid);
        params.put("longResids", longResids);
        return updateTemplate.insert( "synShip_InsertReservationLogByInID", params);
    }
}
