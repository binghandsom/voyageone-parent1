package com.voyageone.batch.ims.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.batch.ims.modelbean.ConditionPropValue;
import com.voyageone.common.Constants;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Leo on 15-9-18.
 */
@Repository
public class ConditionPropValueDao extends BaseDao{
    public List<ConditionPropValue> selectConditionPropValue(String orderChannelId, String platformPropId) {
        return selectList(Constants.DAO_NAME_SPACE_IMS + "", parameters("channel_id", orderChannelId, "platform_prop_id", platformPropId));
    }
}
