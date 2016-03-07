package com.voyageone.task2.cms.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.task2.cms.model.CustomPlatformPropMappingModel;
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
    public List<CustomPlatformPropMappingModel> getCustomMappingPlatformProps(int cartId)
    {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("cart_id", cartId);
        return updateTemplate.selectList(Constants.DAO_NAME_SPACE_CMS + "cms_selectCustomMappingPlatformProp", cartId);
    }

    public List<CustomPlatformPropMappingModel> insertCustomMapping(CustomPlatformPropMappingModel customPlatformPropMappingModel)
    {
        return updateTemplate.selectList(Constants.DAO_NAME_SPACE_CMS + "cms_insertCustomMapping", customPlatformPropMappingModel);
    }
}
