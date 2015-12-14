package com.voyageone.batch.cms.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.common.Constants;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Leo on 15-7-13.
 */
@Repository
public class SkuInventoryDao extends BaseDao{
    public Map<String, Integer> getSkuInventory(String channelId, List<String> skus)
    {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("channel_id", channelId);
        dataMap.put("skus", skus);

        return selectMap(Constants.DAO_NAME_SPACE_CMS + "cms_selectInventoryBySku", dataMap, "");
    }
}
