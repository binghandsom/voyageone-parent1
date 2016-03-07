package com.voyageone.task2.cms.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.common.Constants;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by Leo on 15-10-28.
 */
@Repository
public class CustomSizePropDao extends BaseDao{
    public List<Map<String, Object>> selectCustomSizeProp(String channelId) {
        List<Map<String, Object>> allCustomSizeMap = selectList(Constants.DAO_NAME_SPACE_CMS + "cms_select_custom_size_prop", parameters("channel_id", channelId));
        return allCustomSizeMap;
    }
}
