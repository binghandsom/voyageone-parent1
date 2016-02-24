package com.voyageone.batch.wms.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.batch.wms.modelbean.ClientTrackingBean;
import com.voyageone.common.Constants;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by fred on 2016/1/7.
 */
@Repository
public class SpThirdReportDao extends BaseDao {
    /**
     * @param items
     * @return
     */
    public int insertClientTrackingInfo(List<ClientTrackingBean> items) {

        Map<String, List<ClientTrackingBean>> paramMap = new HashMap<>();
        paramMap.put("items", items);
        //唯一KEY重复的情况下，跳过。不存在的情况下插入
        return updateTemplate.insert(Constants.DAO_NAME_SPACE_WMS + "wms_insertClientTrackingInfo", paramMap);
    }

    /**
     * 根据order_number获取订单状态
     */
    public int selectCountByOrderNumber(String source_order_id, String order_number, String order_channel_id) {
        return selectOne(Constants.DAO_NAME_SPACE_WMS + "wms_selectCountByOrderNumber", parameters("source_order_id", source_order_id, "order_number", order_number, "order_channel_id", order_channel_id));
    }

    /**
     * 根据source_order_id获取订单状态
     */
    public int selectTrackingBySourcOrderId(String source_order_id, String order_number, String order_channel_id) {
        return selectOne(Constants.DAO_NAME_SPACE_WMS + "wms_selectTrackingBySourcOrderId", parameters("source_order_id", source_order_id, "order_number", order_number, "order_channel_id", order_channel_id));
    }

}
