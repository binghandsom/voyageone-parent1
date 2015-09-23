package com.voyageone.batch.ims.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.batch.ims.modelbean.CustomPlatformPropMapping;
import com.voyageone.common.Constants;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Leo on 15-6-29.
 */
@Repository
public class PlatformPropCustomMappingDao extends BaseDao{
    public List<CustomPlatformPropMapping> getCustomMappingPlatformProps(int cartId)
    {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("cart_id", cartId);
        return updateTemplate.selectList(Constants.DAO_NAME_SPACE_IMS + "ims_selectCustomMappingPlatformProp", cartId);
    }

    public List<CustomPlatformPropMapping> insertCustomMapping(CustomPlatformPropMapping customPlatformPropMapping)
    {
        return updateTemplate.selectList(Constants.DAO_NAME_SPACE_IMS + "ims_insertCustomMapping", customPlatformPropMapping);
    }
}
