package com.voyageone.task2.cms.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.common.Constants;
import com.voyageone.task2.cms.bean.ProductPublishBean;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ProductPublishDao extends BaseDao {
    public ProductPublishBean selectByChannelCartCode(String channel_id, int cart_id, String code) {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("channel_id", channel_id);
        dataMap.put("cart_id", cart_id);
        dataMap.put("code", code);
        return selectOne(Constants.DAO_NAME_SPACE_CMS + "ims_selectProductPublishByChannelCartCode", dataMap);
    }

    public void insertProductPublish(ProductPublishBean productPublishBean, String creater) {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("p", productPublishBean);
        dataMap.put("creater", creater);
        update(Constants.DAO_NAME_SPACE_CMS + "ims_insertProductPublishMini", dataMap);
    }

    public void updateProductPublish(ProductPublishBean productPublishBean) {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("p", productPublishBean);
        update(Constants.DAO_NAME_SPACE_CMS + "ims_updateProductPublishMini", dataMap);
    }

    //  从oms系统导入产品前90天订单信息
    public List<Map> selectProductOrderCount(long oIdx, long oLimit) {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("oIdx", oIdx);
        dataMap.put("oLimit", oLimit);
        return selectList(Constants.DAO_NAME_SPACE_CMS + "cms_selectProductOrderCount", dataMap);
    }


    //  从oms系统导入产品所有订单信息
    public List<Map> selectProductOrderCountAllDays(long oIdx, long oLimit) {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("oIdx", oIdx);
        dataMap.put("oLimit", oLimit);
        return selectList(Constants.DAO_NAME_SPACE_CMS + "cms_selectProductOrderCountAllDays_history", dataMap);
    }

    //  从oms系统导入产品所有订单信息
    public List<Map> selectProductOrderCountAllDaysHistory(long oIdx, long oLimit) {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("oIdx", oIdx);
        dataMap.put("oLimit", oLimit);
        return selectList(Constants.DAO_NAME_SPACE_CMS + "cms_selectProductOrderCountAllDays_history", dataMap);
    }

}
