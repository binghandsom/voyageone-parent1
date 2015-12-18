package com.voyageone.batch.ims.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.common.Constants;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Leo on 2015/5/28.
 */
@Repository
public class BrandMapDao extends BaseDao{

    public String cmsBrandToPlatformBrand(String channel_id, int cart_id, int cmsBrandId)
    {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("channel_id", channel_id);
        dataMap.put("cart_id", cart_id);
        dataMap.put("cms_brand_id", cmsBrandId);

        String brand_id = selectOne(Constants.DAO_NAME_SPACE_IMS + "ims_selectBrandByCmsBrand", dataMap);
        if (brand_id == null)
        {
            return null;
        }
        else {
            System.out.println("brand:" + brand_id);
        }
        return brand_id;
    }
}
