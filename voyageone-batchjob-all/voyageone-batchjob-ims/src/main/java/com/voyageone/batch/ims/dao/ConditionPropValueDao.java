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
    public List<ConditionPropValue> selectAllConditionPropValue() {
        return selectList(Constants.DAO_NAME_SPACE_IMS + "ims_bt_select_all_condition_prop_value");
    }
}
