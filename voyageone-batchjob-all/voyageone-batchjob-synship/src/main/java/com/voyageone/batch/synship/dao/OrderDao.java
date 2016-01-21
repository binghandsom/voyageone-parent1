package com.voyageone.batch.synship.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.batch.synship.modelbean.Order;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class OrderDao extends BaseDao {

    @Override
    protected String namespace() {
        return "com.voyageone.batch.synship.sql";
    }

    /**
     *
     * @param syn_ship_no
     * @param status
     * @param before_status
     * @param task_name
     * @return
     */
    public int updateOrderByStatus(String syn_ship_no, String status, String before_status, String task_name) {

        Map<String, Object> params = new HashMap<>();

        params.put("syn_ship_no", syn_ship_no);
        params.put("status", status);
        params.put("before_status", before_status);
        params.put("task_name", task_name);

        return updateTemplate.update("synShip_UpdateOrderByStatus", params);
    }

    /**
     *
     * @param syn_ship_no
     * @param status
     * @param task_name
     * @return
     */
    public int updateOrderStatus(String syn_ship_no, String status, String task_name) {

        Map<String, Object> params = new HashMap<>();

        params.put("syn_ship_no", syn_ship_no);
        params.put("status", status);
        params.put("task_name", task_name);

        return updateTemplate.update("synShip_UpdateOrderStatus", params);
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

        return updateTemplate.insert("oms_bt_notes_insert", params);
    }

    /**
     *
     * @param order_number
     * @param task_name
     * @return
     */
    public int UpdateOrderCancelFlg(long order_number, String task_name) {

        Map<String, Object> params = new HashMap<>();

        params.put("order_number", order_number);
        params.put("task_name", task_name);

        return updateTemplate.update("oms_UpdateOrderCancelFlg", params);
    }

    /**
     * OMS 订单取得
     * @param sourceOrderIdList
     * @return
     */
    public List<Order> getOrdersListBySourceOrderIdList(List<String> sourceOrderIdList) {
        Map<String, Object> params = new HashMap<>();
        params.put("sourceOrderIdList", sourceOrderIdList);
        return selectList("synShip_SelectOrdersBySourceOrderIdList", params);
    }
}
