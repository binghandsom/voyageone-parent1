package com.voyageone.batch.ims.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.batch.ims.enums.ImsPicCategoryType;
import com.voyageone.batch.ims.modelbean.ImsPicCategory;
import com.voyageone.common.configs.beans.ShopBean;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 对表 ims_bt_pic_category 操作
 * <p>
 * Created by Jonas on 8/10/15.
 */
@Repository
public class ImsPicCategoryDao extends BaseDao {

    @Override
    protected String namespace() {
        return "com.voyageone.ims.sql";
    }

    /**
     * 查询所有的 category
     */
    public List<ImsPicCategory> select() {
        return selectList("ims_bt_pic_category_select");
    }

    /**
     * 更新 category 的 category_tid
     */
    public int updateTid(ImsPicCategory category) {
        return update("ims_bt_pic_category_updateTid", category);
    }

    public ImsPicCategory select(ShopBean shopBean, ImsPicCategoryType type) {
        return selectOne("ims_bt_pic_category_select_byCart", parameters(
                "cart_id", shopBean.getCart_id(),
                "channel_id", shopBean.getOrder_channel_id(),
                "type", type.getVal()));
    }
}
