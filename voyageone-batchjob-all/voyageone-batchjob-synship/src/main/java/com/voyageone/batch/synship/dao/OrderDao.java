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
