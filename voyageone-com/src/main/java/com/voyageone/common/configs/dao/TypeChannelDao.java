package com.voyageone.common.configs.dao;


import com.voyageone.base.dao.BaseDao;
import com.voyageone.common.Constants;
import com.voyageone.common.configs.beans.TypeChannelBean;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Jonas on 4/10/2015.
 */
@Repository
public class TypeChannelDao extends BaseDao {
    /**
     * 根据消息类型获得类型消息Map
     */
    public List<TypeChannelBean> getAll() {
        return selectList(Constants.DAO_NAME_SPACE_COMMON + "com_mt_value_channel_getAll");
    }

    public List<Map<String, Object>> getByChannelTypeId(String channelId, Integer typeId, String value, Boolean status, Integer start, Integer length) {
        Map<String, Object> param = new HashMap();
        param.put("channelId", channelId);
        param.put("typeId", typeId);
        param.put("status",status);
        param.put("value", value);
        param.put("start", start);
        param.put("length", length);
        return selectList(Constants.DAO_NAME_SPACE_COMMON + "com_mt_value_channel_by_typeId", param);
    }

    public Integer getByChannelTypeIdCnt(String channelId, Integer typeId, String value, Boolean status) {
        Map<String, Object> param = new HashMap();
        param.put("channelId", channelId);
        param.put("typeId", typeId);
        param.put("status",status);
        param.put("value", value);
        return selectOne(Constants.DAO_NAME_SPACE_COMMON + "com_mt_value_channel_by_typeId_cnt", param);
    }


}
