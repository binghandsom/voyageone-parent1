package com.voyageone.task2.cms.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.common.Constants;
import com.voyageone.task2.cms.bean.InventoryForCmsBean;
import com.voyageone.task2.cms.bean.SkuInventoryForCmsBean;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class InventoryDao extends BaseDao {

    /**
     * 获取code级别的库存
     * @param order_channel_id
     * @return
     */
    public List<InventoryForCmsBean> selectInventoryCode(String order_channel_id, String modified) {
        Map<String, Object> params = new HashMap<>();

        params.put("order_channel_id", order_channel_id);
        params.put("modified", modified);
        params.put("creater", modified);

        return selectList(Constants.DAO_NAME_SPACE_CMS + "cms_selectInventoryCode", params);
    }

    /**
     * 批量查询sku级别库存
     * @param order_channel_id
     * @param codes
     * @return
     */
    public List<SkuInventoryForCmsBean> batchSelectInventory (String order_channel_id, List<String> codes) {
        if (StringUtils.isNotBlank(order_channel_id) && CollectionUtils.isNotEmpty(codes)) {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("order_channel_id", order_channel_id);
            params.put("codes", codes);
            return selectList(Constants.DAO_NAME_SPACE_CMS + "cms_batchSelectInventory", params);
        }
        return null;
    }

    public List<SkuInventoryForCmsBean> batchSelectInventory (String order_channel_id, int offset, int size) {
        if (StringUtils.isNotBlank(order_channel_id) ) {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("order_channel_id", order_channel_id);
            params.put("offset", offset);
            params.put("size", size);
            return selectList(Constants.DAO_NAME_SPACE_CMS + "cms_batchSelectInventory2", params);
        }
        return null;
    }


}
