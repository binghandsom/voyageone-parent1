package com.voyageone.batch.ims.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.common.Constants;
import org.springframework.stereotype.Repository;

import java.util.*;

/**
 * Created by Leo on 15-10-28.
 */
@Repository
public class CustomSizePropDao extends BaseDao{
    public List<Map<String, Object>> selectCustomSizeProp(String channelId) {
        List<Map<String, Object>> allCustomSizeMap = selectList(Constants.DAO_NAME_SPACE_IMS + "ims_select_custom_size_prop", parameters("channel_id", channelId));
        return allCustomSizeMap;
    }
}
