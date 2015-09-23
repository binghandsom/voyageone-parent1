package com.voyageone.batch.synship.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.batch.synship.modelbean.ClientTrackingBean;
import com.voyageone.batch.synship.modelbean.ClientTrackingSimBean;
import com.voyageone.batch.synship.modelbean.TrackingSyncBean;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ClientTrackingDao extends BaseDao {

    @Override
    protected String namespace() {
        return "com.voyageone.batch.synship.sql";
    }

    /**
     * 获取无法模拟的记录
     *
     * @param order_channel_id 渠道
     * @return String
     */
    public List<String> getErrorClientTrackingLst(String order_channel_id) {
        Map<String, Object> params = new HashMap<>();

        params.put("order_channel_id", order_channel_id);

        return selectList("synShip_getErrorClientTrackingLst", params);
    }

    /**
     * 获取需要进行模拟的记录
     *
     * @param order_channel_id 渠道
     * @param row_count        抽出件数
     * @return ClientTrackingSimBean
     */
    public List<ClientTrackingSimBean> getClientTrackingSimLst(String order_channel_id, int row_count) {
        Map<String, Object> params = new HashMap<>();

        params.put("order_channel_id", order_channel_id);
        params.put("limit", row_count);

        return selectList("synShip_getClientTrackingSimLst", params);
    }

    /**
     * @param items
     * @return
     */
    public int insert(List<ClientTrackingBean> items) {

        Map<String, List<ClientTrackingBean>> paramMap = new HashMap<>();
        paramMap.put("items", items);
        //唯一KEY重复的情况下，跳过。不存在的情况下插入
        return updateTemplate.insert("synShip_insertClientTrackingInfo", paramMap);
    }

    /**
     * 更新真正品牌方订单号（对于第三方订单，有时会返回两个品牌方订单号，一个是代理的，一个是真正的）
     * @param order_channel_id
     * @param client_order_id
     * @param seller_order_id
     * @return
     */
    public int updateSellerOrderID(String order_channel_id,String client_order_id, String seller_order_id, String taskName) {

        Map<String,String> paramMap = new HashMap<>();
        paramMap.put("order_channel_id", order_channel_id);
        paramMap.put("client_order_id", client_order_id);
        paramMap.put("seller_order_id", seller_order_id);
        paramMap.put("taskName", taskName);

        //更新真正品牌方订单号
        return updateTemplate.insert("synShip_updateSellerOrderID", paramMap);
    }

    /**
     *
     * @param seq
     * @param sim_flg
     * @param task_name
     * @return
     */
    public int UpdatClientTrackingSimFlg(long seq, String sim_flg, String task_name) {

        Map<String, Object> params = new HashMap<>();

        params.put("seq", seq);
        params.put("sim_flg", sim_flg);
        params.put("task_name", task_name);

        return updateTemplate.insert( "synShip_UpdateClientTrackingSimFlg", params);
    }


}
