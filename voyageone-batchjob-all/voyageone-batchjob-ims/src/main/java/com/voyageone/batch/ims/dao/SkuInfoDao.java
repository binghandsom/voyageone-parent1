package com.voyageone.batch.ims.dao;

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
public class SkuInfoDao extends BaseDao{
    public List getSkuInfo(String channelId, String code)
    {
        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("channel_id", channelId);
        dataMap.put("code", code);
        return selectList(Constants.DAO_NAME_SPACE_IMS + "ims_selectSkuInfoByCode", dataMap);
    }

    public String getSkuSize(String channelId, String code, String sku)
    {
        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("channel_id", channelId);
        dataMap.put("code", code);
        dataMap.put("sku", sku);
        return selectOne(Constants.DAO_NAME_SPACE_IMS + "ims_selectSkuSizeBySku", dataMap);
    }

    public String getSkuInventory(String channelId, String code, String sku)
    {
        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("channel_id", channelId);
        dataMap.put("code", code);
        if (sku != null) {
            dataMap.put("sku", sku);
        }
        return selectOne(Constants.DAO_NAME_SPACE_IMS + "ims_selectInventoryByCondition", dataMap);
    }
}
