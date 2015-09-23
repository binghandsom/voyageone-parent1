package com.voyageone.batch.ims.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.batch.ims.modelbean.PropValueBean;
import com.voyageone.common.Constants;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Leo on 15-6-29.
 */
@Repository
public class PropValueDao extends BaseDao{
    public List<PropValueBean> selectPropValue(String channelId, int level, String levelValue, String propId)
    {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("channel_id", channelId);
        dataMap.put("level", level);
        dataMap.put("level_value", levelValue);
        dataMap.put("prop_id", propId);
        List<PropValueBean> propValues = selectList(Constants.DAO_NAME_SPACE_IMS + "ims_mt_prop_value_get_value", dataMap);

        if (propValues.size() != 0) {
            return propValues;
        }
        return null;
    }

    public PropValueBean selectPropValueByUUID(String propHash)
    {
        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("prop_hash", propHash);

        return selectOne(Constants.DAO_NAME_SPACE_IMS + "ims_mt_prop_value_get_value_by_uuid", dataMap);
    }

    public void insertPropValue(PropValueBean propValueBean)
    {
        updateTemplate.insert(Constants.DAO_NAME_SPACE_IMS + "ims_mt_prop_value_insert_value", propValueBean);
    }
}
