package com.voyageone.batch.ims.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.batch.ims.modelbean.ProductPublishBean;
import com.voyageone.common.Constants;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Leo on 15-7-24.
 */
@Repository
public class ProductPublishDao extends BaseDao{
    public List<ProductPublishBean> selectNoPublishBeansByTime(int recordCount) {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("count", recordCount);
        return selectList(Constants.DAO_NAME_SPACE_IMS + "ims_selectNoPublishBeansByTime", dataMap);
    }

    public List<ProductPublishBean> selectProductPublishByCondition(int cartId, String channelId, int modelId, int groupId, Boolean isPublished) {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("cart_id", cartId);
        dataMap.put("channel_id", channelId);
        dataMap.put("model_id", modelId);
        dataMap.put("cn_group_id", groupId);
        //如果isPublished为null，那么不把该字段作为条件
        if (isPublished != null) {
            if (isPublished)
                dataMap.put("is_published", 1);
            else
                dataMap.put("is_published", 0);
        }
        return selectList(Constants.DAO_NAME_SPACE_IMS + "ims_selectProductPublishByCondition", dataMap);
    }

    public void updateProductPublish(ProductPublishBean productPublishBean) {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("cart_id", productPublishBean.getCart_id());
        dataMap.put("channel_id", productPublishBean.getChannel_id());
        dataMap.put("product_id", productPublishBean.getProduct_id());
        dataMap.put("is_published", productPublishBean.getIs_published());
        dataMap.put("publish_status", productPublishBean.getPublish_status());
        dataMap.put("publish_product_status", productPublishBean.getPublish_product_status());
        dataMap.put("publish_datetime", productPublishBean.getPublish_date_time());
        dataMap.put("num_iid", productPublishBean.getNum_iid());
        dataMap.put("publish_failed_comment", productPublishBean.getPublish_failed_comment());
        dataMap.put("publish_product_id", productPublishBean.getPublish_product_id());
        dataMap.put("main_product_flg", productPublishBean.getMain_product_flg());
        dataMap.put("quanity_update_type", productPublishBean.getQuantity_update_type());
        update(Constants.DAO_NAME_SPACE_IMS + "ims_updateProductPublish", dataMap);
    }
}
