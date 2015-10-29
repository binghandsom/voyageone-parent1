package com.voyageone.batch.ims.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.batch.ims.enums.SkuPropNameEnum;
import com.voyageone.common.Constants;
import org.springframework.stereotype.Repository;

/**
 * Created by Leo on 15-9-17.
 */
@Repository
public class SkuPropValueDao extends BaseDao{
    public String selectSkuPropValue(String orderChannelId, String sku, String propName) {
        return selectOne(Constants.DAO_NAME_SPACE_IMS + "ims_bt_prop_value_sku_get_value", parameters("order_channel_id", orderChannelId, "sku", sku, "prop_name", propName));
    }
}
