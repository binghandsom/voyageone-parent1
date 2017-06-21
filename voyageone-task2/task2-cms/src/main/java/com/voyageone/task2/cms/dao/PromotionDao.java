package com.voyageone.task2.cms.dao;

import com.voyageone.base.dao.BaseDao;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by james.li on 2015/10/29.
 */
@Repository
public class PromotionDao extends BaseDao {
    /**
     * 取得需要添加折扣的产品列表
     * @param channelId
     * @param cartId
     * @return
     */
    public List<Map> getPromotionItem(String channelId, String cartId){
        Map<String,Object> parameter = new HashMap<>();
        parameter.put("channelId",channelId);
        parameter.put("cartId", cartId);
        List<Integer> promotionIds = getPendingPromotionId(channelId, cartId);
        List<Map> data = null;
        if(promotionIds != null && promotionIds.size() > 0){
            parameter.put("promotionIds", promotionIds);
            data = selectList("cms_promotion_select", parameter);
        }
        if (data == null){
            data = new ArrayList<>();
        }
        return data;
    }

    public List<Map> getPromotionItem(Integer promotionId){
        List<Map> data = selectList("select_pending_product", promotionId);
        if (data == null){
            data = new ArrayList<>();
        }
        return data;
    }

    public int updatePromotionStatus(Map<String,Object> parameter){
        return update("com.voyageone.cms.sql.update_cms_bt_promotion_task", parameter);
    }

    public List<Integer> getPendingPromotionId(String channelId, String cartId){
        Map<String,Object> parameter = new HashMap<>();
        parameter.put("channelId",channelId);
        parameter.put("cartId", cartId);

        List<Integer> data = selectList("select_pending_promotion", parameter);

        if (data == null){
            data = new ArrayList<>();
        }
        return data;
    }
}
