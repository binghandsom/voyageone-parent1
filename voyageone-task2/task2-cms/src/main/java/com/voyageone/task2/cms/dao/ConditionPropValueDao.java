package com.voyageone.task2.cms.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.task2.cms.model.ConditionPropValueModel;
import com.voyageone.common.Constants;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Leo on 15-9-18.
 */
@Repository
public class ConditionPropValueDao extends BaseDao{
//    public List<ConditionPropValueModel> selectAllConditionPropValue() {
//        return selectList(Constants.DAO_NAME_SPACE_CMS + "cms_bt_select_all_condition_prop_value");
//    }

    public List<ConditionPropValueModel> selectConditionPropValueByChannelId(String channelId) {
        return selectList(Constants.DAO_NAME_SPACE_CMS + "cms_bt_select_condition_prop_value_by_channel_id", parameters("channel_id", channelId));
    }
}
