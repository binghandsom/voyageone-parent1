package com.voyageone.batch.ims.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.batch.ims.modelbean.PlatformSkuInfoBean;
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

    public List<PlatformSkuInfoBean> selectPlatformSkuInfo(String propId, int cartId)
    {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("prop_id", propId);
        dataMap.put("cart_id", cartId);
        return selectList(Constants.DAO_NAME_SPACE_CMS + "cms_selectSkuInfoByPropId", dataMap);
    }

    public void insertPlatformSkuInfo(PlatformSkuInfoBean tmallSkuInfo)
    {
        updateTemplate.insert(Constants.DAO_NAME_SPACE_CMS + "cms_insertSkuInfo", tmallSkuInfo);
    }
}
