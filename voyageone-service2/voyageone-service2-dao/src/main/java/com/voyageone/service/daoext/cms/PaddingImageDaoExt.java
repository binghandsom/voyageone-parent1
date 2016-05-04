package com.voyageone.service.daoext.cms;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.common.Constants;
import org.springframework.stereotype.Repository;

/**
 * Created by morse.lu on 16-4-26.(copy from task2)
 */
@Repository
public class PaddingImageDaoExt extends BaseDao{

    public String selectByCriteria(String orderChannelId, int cartId, String paddingPropName, int imageIndex) {
        return selectOne(Constants.DAO_NAME_SPACE_CMS + "cms_bt_padding_image_select_by_criteria", parameters("order_channel_id", orderChannelId, "cart_id", cartId, "padding_prop_name", paddingPropName, "padding_index", imageIndex));
    }
}
