package com.voyageone.batch.synship.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.batch.synship.modelbean.EtkTrackingBean;
import com.voyageone.batch.synship.modelbean.PackageItemBean;
import com.voyageone.batch.synship.modelbean.TrackingBean;
import com.voyageone.batch.synship.modelbean.TrackingSyncBean;
import com.voyageone.common.Constants;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class TrackingDao extends BaseDao {

    @Override
    protected String namespace() {
        return "com.voyageone.batch.synship.sql";
    }

    /**
     * 获取需要进行的库存同步记录
     *
     * @param order_channel_id 渠道
     * @param cart_id          店铺
     * @param row_count        抽出件数
     * @return TrackingSyncBean
     */
    public List<TrackingSyncBean> getSyncTracking(String task_name, String order_channel_id, String cart_id, int row_count) {
        Map<String, Object> params = new HashMap<>();

        params.put("task_name", task_name);
        params.put("order_channel_id", order_channel_id);
        params.put("cart_id", cart_id);
        params.put("limit", row_count);

        return selectList("synShip_getSyncTracking", params);
    }

    /**
     * 获取需要进行的库存同步记录(模拟)
     *
     * @param order_channel_id 渠道
     * @param cart_id          店铺
     * @param row_count        抽出件数
     * @return TrackingSyncBean
     */
    public List<TrackingSyncBean> getSimSyncTracking(String task_name, String order_channel_id, String cart_id, int row_count) {
        Map<String, Object> params = new HashMap<>();

        params.put("task_name", task_name);
        params.put("order_channel_id", order_channel_id);
        params.put("cart_id", cart_id);
        params.put("limit", row_count);

        return selectList("synShip_getSimSyncTracking", params);
    }

     /**
     * 同步运单
     *
     * @param trackingSyncBean 物流信息
     */
    public int UpdateSyncTracking(TrackingSyncBean trackingSyncBean) {

        return updateTemplate.insert( "synShip_UpdateSyncTracking", trackingSyncBean);
    }

    /**
     *
     * @param order_channel_id
     * @param tracking_type
     * @param tracking_no
     * @param syn_ship_no
     * @return
     */
    public TrackingBean getTracking(String order_channel_id, String tracking_type, String tracking_no, String syn_ship_no) {
        Map<String, Object> params = new HashMap<>();

        params.put("order_channel_id", order_channel_id);
        params.put("tracking_type", tracking_type);
        params.put("tracking_no", tracking_no);
        params.put("syn_ship_no", syn_ship_no);

        return updateTemplate.selectOne("synShip_getTracking", params);
    }

    /**
     * 插入Tracking
     *
     * @param trackingBean 物流信息
     */
    public int insertTracking(TrackingBean trackingBean) {

        return updateTemplate.insert( "synShip_InsertTracking", trackingBean);
    }

    /**
     *
     * @param syn_ship_no
     * @param tracking_no
     * @param tracking_type
     * @param task_name
     * @return
     */
    public int insertResTracking(String syn_ship_no, String tracking_no, String tracking_type, String task_name) {

        Map<String, Object> params = new HashMap<>();

        params.put("syn_ship_no", syn_ship_no);
        params.put("tracking_no", tracking_no);
        params.put("tracking_type", tracking_type);
        params.put("task_name", task_name);

        return updateTemplate.insert( "synShip_InsertResTracking", params);
    }

    /**
     *
     * @param syn_ship_no
     * @param tracking_no
     * @param process_time
     *  * @param tracking_status
     * @param task_name
     * @return
     */
    public int insertTrackingInfo(String syn_ship_no, String tracking_no, String tracking_status, String process_time, String status, String task_name) {

        Map<String, Object> params = new HashMap<>();

        params.put("syn_ship_no", syn_ship_no);
        params.put("tracking_no", tracking_no);
        params.put("tracking_status", tracking_status);
        params.put("process_time", process_time);
        params.put("status", status);
        params.put("task_name", task_name);

        return updateTemplate.insert( "synShip_InsertTrackingInfo", params);
    }

    /**
     *
     * @param syn_ship_no
     * @param tracking_no
     * @param process_time
     *  * @param tracking_status
     * @param task_name
     * @return
     */
    public int insertTrackingInfoBySim(String syn_ship_no, String tracking_no, String tracking_status, String process_time, String task_name) {

        Map<String, Object> params = new HashMap<>();

        params.put("syn_ship_no", syn_ship_no);
        params.put("tracking_no", tracking_no);
        params.put("tracking_status", tracking_status);
        params.put("process_time", process_time);
        params.put("task_name", task_name);

        return updateTemplate.insert( "synShip_InsertTrackingInfoBySim", params);
    }

    /**
     * 获取需要进行轮询的ETK面单记录(
     *
     * @return EtkTrackingBean
     */
    public List<EtkTrackingBean> getEtkTracking() {

        return selectList("synShip_getEtkTracking");
    }

    /**
     *
     * @param tracking_type
     * @param tracking_no
     * @param sent_kd100_poll_flg
     * @return
     */
    public int updateKD100Poll( String tracking_type, String tracking_no, String sent_kd100_poll_flg, String task_name) {
        Map<String, Object> params = new HashMap<>();

        params.put("tracking_type", tracking_type);
        params.put("tracking_no", tracking_no);
        params.put("sent_kd100_poll_flg", sent_kd100_poll_flg);
        params.put("task_name", task_name);

        return updateTemplate.update("synShip_UpdateKD100Poll", params);
    }

    /**
     * 查询订单是否有物流信息
     * @param order_channel_id 订单渠道
     * @param processTime 比较的处理日
     * @return List<String>
     */
    public List<String> getNotTrackingList(String order_channel_id, String processTime) {

        Map<String, Object> params = new HashMap<>();

        params.put("order_channel_id", order_channel_id);
        params.put("processTime", processTime);

        return updateTemplate.selectList("synShip_tracking_info_getNotTracking", params);
    }

    /**
     * 查询物流信息
     * @param synShipNo synShipNo
     * @param trackingStatus 物品的物流状态
     * @return int
     */
    public int selectTrackingByStatus(String synShipNo, String trackingStatus) {

        Map<String, Object> params = new HashMap<>();

        params.put("synShipNo", synShipNo);
        params.put("trackingStatus", trackingStatus);

        return updateTemplate.selectOne("synShip_tracking_info_selectByStatus", params);
    }

    /**
     * 插入物流信息
     * @param synShipNo synShipNo
     * @param reservationId reservationId
     * @param trackingStatus 物品的物流状态
     * @param userName 更新者
     * @return int
     */
    public int insertTrackingByStatus(String synShipNo, String trackingStatus, long reservationId, String userName) {

        Map<String, Object> params = new HashMap<>();

        params.put("synShipNo", synShipNo);
        params.put("reservationId", reservationId);
        params.put("trackingStatus", trackingStatus);
        params.put("userName", userName);

        return updateTemplate.insert("synShip_tracking_info_insertByStatus", params);
    }


}
