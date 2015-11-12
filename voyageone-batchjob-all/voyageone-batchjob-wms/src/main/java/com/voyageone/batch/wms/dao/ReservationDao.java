package com.voyageone.batch.wms.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.batch.wms.modelbean.ReservationBean;
import com.voyageone.common.Constants;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ReservationDao extends BaseDao {

    /**
     * 在ItemDetail中不存在的记录取得
     * @param OrderChannelID 订单渠道
     * @return List<String>
     */
    public List<String> getNotExistsItemCode(String OrderChannelID) {

        Map<String, Object> params = new HashMap<>();

        params.put("order_channel_id", OrderChannelID);

        return selectList(Constants.DAO_NAME_SPACE_WMS + "wms_selectNotExistsItemCode", params);

    }

    /**
     * 需要库存分配的记录取得（物品级别）
     * @param OrderChannelID 订单渠道
     * @param row_count 抽出件数
     * @return List<ReservationBean>
     */
    public List<ReservationBean> getAllotInventoryInto(String OrderChannelID,int row_count) {

        Map<String, Object> params = new HashMap<>();

        params.put("order_channel_id", OrderChannelID);
        params.put("limit", row_count);

        return selectList(Constants.DAO_NAME_SPACE_WMS + "wms_selectAllotInventoryInto", params);

    }

    /**
     * 需要库存分配的记录取得（订单级别）
     * @param OrderChannelID 订单渠道
     * @param row_count 抽出件数
     * @return List<ReservationBean>
     */
    public List<ReservationBean> getAllotInventoryIntoByOrder(String OrderChannelID,int row_count) {

        Map<String, Object> params = new HashMap<>();

        params.put("order_channel_id", OrderChannelID);
        params.put("limit", row_count);

        return selectList(Constants.DAO_NAME_SPACE_WMS + "wms_selectAllotInventoryIntoByOrder", params);

    }

    /**
     * 需要库存分配的记录对应仓库取得
     * @param OrderChannelID 订单渠道
     * @param CartId CartId
     * @param Sku Sku
     * @return long
     */
    public long getAllotStore(String OrderChannelID,String CartId, String Sku) {

        Map<String, Object> params = new HashMap<>();

        params.put("order_channel_id", OrderChannelID);
        params.put("cart_id", CartId);
        params.put("sku", Sku);

        return updateTemplate.selectOne(Constants.DAO_NAME_SPACE_WMS + "wms_getAllotStore", params);

    }

    /**
     * 需要库存分配的记录对应仓库取得(排除一些不需要的仓库)
     * @param OrderChannelID 订单渠道
     * @param CartId CartId
     * @param Sku Sku
     * @return long
     */
    public long getAllotStoreAgain(String OrderChannelID,String CartId, String Sku, String storeList) {

        Map<String, Object> params = new HashMap<>();

        params.put("order_channel_id", OrderChannelID);
        params.put("cart_id", CartId);
        params.put("sku", Sku);
        params.put("storeList", storeList);

        return updateTemplate.selectOne(Constants.DAO_NAME_SPACE_WMS + "wms_getAllotStoreAgain", params);

    }

    /**
     * 发货渠道的取得
     * @param order_channel_id 订单渠道
     * @param order_number 订单号
     * @param reservationId
     * @return 物品ID
     */
    public String getShippingMethod(String order_channel_id, long order_number, long reservationId){

        Map<String, Object> params = new HashMap<>();

        params.put("order_channel_id", order_channel_id);
        params.put("order_number", order_number);
        params.put("reservationId", reservationId);

        return updateTemplate.selectOne(Constants.DAO_NAME_SPACE_WMS + "wms_getShippingMethod", params);

    }

    /**
     * 插入Reservation
     * @param reservation reservation记录
     * @return long
     */
    public long insertReservation(ReservationBean reservation) {

        long resId = 0;
        int retCount =  updateTemplate.insert(Constants.DAO_NAME_SPACE_WMS + "wms_reservation_insert", reservation);

        if (retCount > 0) {
            resId = reservation.getId();
        }
        return resId;

    }

    /**
     * 更新Reservation的发货渠道
     * @param orderNumber 订单号
     * @param reservationID reservationID
     * @param shipChannel 发货渠道
     * @return long
     */
    public long updateShipChannel(long orderNumber, long reservationID, String shipChannel) {

        Map<String, Object> params = new HashMap<>();

        params.put("orderNumber", orderNumber);
        params.put("reservationID", reservationID);
        params.put("shipChannel", shipChannel);

        return updateTemplate.update(Constants.DAO_NAME_SPACE_WMS + "wms_updateReservationShipChannel", params);

    }

    /**
     * 更新Reservation的状态
     * @param orderNumber 订单号
     * @param reservationList reservationID一览
     * @param status 状态
     * @return long
     */
    public long updateReservationStatus(long orderNumber, List<Long> reservationList, String status) {

        Map<String, Object> params = new HashMap<>();

        params.put("orderNumber", orderNumber);
        params.put("reservationList", reservationList);
        params.put("status", status);

        return updateTemplate.update(Constants.DAO_NAME_SPACE_WMS + "wms_updateReservationStatus", params);

    }

    /**
     * OrderDetails的更新
     * @param orderNumber 订单号
     * @param itemNumber itemNumber
     * @param reservationID reservationID
     * @param takeName 任务名
     * @return int
     */
    public int updateOrderDetails(long orderNumber, long itemNumber, long reservationID, String takeName) {

        Map<String, Object> params = new HashMap<>();

        params.put("orderNumber", orderNumber);
        params.put("itemNumber", itemNumber);
        params.put("reservationID", reservationID);
        params.put("takeName", takeName);

        return updateTemplate.update(Constants.DAO_NAME_SPACE_WMS + "wms_updateOrderDetails", params);
    }

    /**
     * 插入oms_bt_notes
     * @param orderNumber 订单号
     * @param source_order_id Web订单号
     * @param note 备注
     * @param takeName 更新者
     * @return int
     */
    public int insertOrderNotes(long orderNumber, String source_order_id, String note, String takeName) {

        Map<String, Object> params = new HashMap<>();

        params.put("orderNumber", orderNumber);
        params.put("source_order_id", source_order_id);
        params.put("note", note);
        params.put("takeName", takeName);

        return updateTemplate.insert(Constants.DAO_NAME_SPACE_WMS + "oms_bt_notes_insert", params);
    }

    /**
     * 更新处理完毕的Reservation记录
     *
     * @param transfer_id 处理的transfer_id
     * @param taskName 任务名
     */
    public int updateReservationCloseDay(long transfer_id, long transfer_item_id, String taskName) {

        Map<String, Object> params = new HashMap<>();

        params.put("transfer_id", transfer_id);
        params.put("transfer_item_id", transfer_item_id);
        params.put("task_name", taskName);

        return updateTemplate.insert(Constants.DAO_NAME_SPACE_WMS + "wms_updateReservationCloseDay", params);
    }

}
