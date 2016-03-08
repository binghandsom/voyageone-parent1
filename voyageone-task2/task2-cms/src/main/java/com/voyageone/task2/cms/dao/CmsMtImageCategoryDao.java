package com.voyageone.task2.cms.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.cms.enums.ImageCategoryType;
import com.voyageone.common.Constants;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.task2.cms.model.CmsMtImageCategoryModel;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by jonasvlag on 16/3/7.
 *
 * @version 2.0.0
 * @since 2.0.0
 */
@Repository
public class CmsMtImageCategoryDao extends BaseDao {
    @Override
    protected String namespace() {
        return Constants.getDaoNameSpace(SubSystem.CMS);
    }

    /**
     * 查询所有的 category
     */
    public List<CmsMtImageCategoryModel> select() {
        return selectList("ims_bt_pic_category_select");
    }

    /**
     * 更新 category 的 category_tid
     */
    public int updateTid(CmsMtImageCategoryModel category) {
        return update("ims_bt_pic_category_updateTid", category);
    }

    public CmsMtImageCategoryModel select(ShopBean shopBean, ImageCategoryType type) {
        return selectOne("ims_bt_pic_category_select_byCart", parameters(
                "cart_id", shopBean.getCart_id(),
                "channel_id", shopBean.getOrder_channel_id(),
                "type", type.getVal()));
    }
}
