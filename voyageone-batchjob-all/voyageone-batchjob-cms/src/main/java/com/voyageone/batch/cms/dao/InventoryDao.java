package com.voyageone.batch.cms.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.batch.cms.model.InventoryModel;
import com.voyageone.common.Constants;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dell on 2015/11/28.
 */
@Repository
public class InventoryDao extends BaseDao {
    /**
     * 获取渠道下，所有code界别逻辑库存
     *
     * @param order_channel_id 渠道
     * @return Inventory 集合
     */
    public List<InventoryModel> getCodeInventory(String order_channel_id) {

        Map<String, Object> params = new HashMap<>();

        params.put("order_channel_id", order_channel_id);

        return selectList(Constants.DAO_NAME_SPACE_CMS + "cms_getLogicInventoryByCode", params);
    }
}
