package com.voyageone.batch.cms.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.batch.cms.bean.*;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ItemDetailsDao extends BaseDao {

    /**
     * 根据code, 获取item details表的数据
     * @param channelId channel id
     * @param code code
     *
     * @return List
     */
    public List<ItemDetailsBean> selectByCode(String channelId, String code) {

        Map<String, Object> params = new HashMap<>();
        params.put("channelId", channelId);
        params.put("code", code);

        return updateTemplate.selectList("wms_bt_item_details_select_by_code", params);
    }

    /**
     * 插入item details的数据
     */
    public int insertItemDetails(ItemDetailsBean itemDetailsBean, String taskName) {

        Map<String, Object> params = new HashMap<>();
        params.put("itemDetailsBean", itemDetailsBean);
        params.put("taskName", taskName);

        return updateTemplate.insert("wms_bt_item_details_insert", params);
    }
}