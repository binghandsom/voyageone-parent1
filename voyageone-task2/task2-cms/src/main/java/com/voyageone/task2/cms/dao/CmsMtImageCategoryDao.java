package com.voyageone.task2.cms.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.common.Constants;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.service.model.cms.enums.ImageCategoryType;
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
        return selectList("cms_mt_image_category_select");
    }

    /**
     * 更新 category 的 category_tid
     */
    public int updateTid(CmsMtImageCategoryModel category) {
        return update("cms_mt_image_category_updateTid", category);
    }

    /**
     * 新建 category
     */
    public int insert(CmsMtImageCategoryModel category) {
        return insert("cms_mt_image_category_insert", category);
    }

    public CmsMtImageCategoryModel select(ShopBean shopBean, ImageCategoryType type) {
        return selectOne("cms_mt_image_category_select_byCart", parameters(
                "cart_id", shopBean.getCart_id(),
                "channel_id", shopBean.getOrder_channel_id(),
                "type", type.getVal()));
    }

    /**
     * 使用主键删除
     */
    public void delete(int category_id) {
        delete("cms_mt_image_category_remove", category_id);
    }
}
