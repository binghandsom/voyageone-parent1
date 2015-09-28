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
    public int insertTrackingInfo(String syn_ship_no, String tracking_no, String tracking_status, String process_time, String task_name) {

        Map<String, Object> params = new HashMap<>();

        params.put("syn_ship_no", syn_ship_no);
        params.put("tracking_no", tracking_no);
        params.put("tracking_status", tracking_status);
        params.put("process_time", process_time);
        params.put("task_name", task_name);

        return updateTemplate.insert( "synShip_InsertTrackingInfo", params);
    }

    /**
     * 获取需要进行轮询的ETK面单记录(
     *
     * @return EtkTrackingBean
     */
    public List<EtkTrackingBean> getEtkTracking() {

        return selectList("synShip_getEtkTracking");
    }


}
