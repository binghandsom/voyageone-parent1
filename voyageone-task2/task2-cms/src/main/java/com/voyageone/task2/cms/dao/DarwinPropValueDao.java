package com.voyageone.task2.cms.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.task2.cms.model.DarwinPropValueModel;
import com.voyageone.common.Constants;
import org.springframework.stereotype.Repository;

/**
 * Created by Leo on 15-9-15.
 */
@Repository
public class DarwinPropValueDao extends BaseDao{
    public DarwinPropValueModel selectDarwinPropValue(int cart_id, String style_code, String platform_prop_id) {
       return selectOne(Constants.DAO_NAME_SPACE_IMS + "cms_mt_darwini_prop_value_get_value", parameters("cart_id", cart_id, "style_code", style_code, "platform_prop_id", platform_prop_id));
    }
}
