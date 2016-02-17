package com.voyageone.batch.cms.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.batch.cms.model.PlatformSkuInfoModel;
import com.voyageone.common.Constants;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Leo on 15-7-17.
 */
@Repository
public class PlatformSkuInfoDao extends BaseDao{

    public List<PlatformSkuInfoModel> selectPlatformSkuInfo(String propId, int cartId)
    {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("prop_id", propId);
        dataMap.put("cart_id", cartId);
        return selectList(Constants.DAO_NAME_SPACE_CMS + "cms_selectSkuInfoByPropId", dataMap);
    }

    public void insertPlatformSkuInfo(PlatformSkuInfoModel tmallSkuInfo)
    {
        updateTemplate.insert(Constants.DAO_NAME_SPACE_CMS + "cms_insertSkuInfo", tmallSkuInfo);
    }
}
