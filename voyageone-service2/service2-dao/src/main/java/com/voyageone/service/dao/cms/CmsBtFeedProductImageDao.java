package com.voyageone.service.dao.cms;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.service.model.cms.CmsBtFeedProductImageModel;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by james.li on 2015/11/30.
 */
@Repository
public class CmsBtFeedProductImageDao extends BaseDao {

    public List<CmsBtFeedProductImageModel> selectImagebyUrl(CmsBtFeedProductImageModel feedImage) {
        return selectList("select_cms_bt_feed_product_image", feedImage);
    }

    public void insertImagebyUrl(CmsBtFeedProductImageModel feedImage) {
        insert("insert_cms_bt_feed_product_image", feedImage);
    }

    public void insertImagebyUrl(List<CmsBtFeedProductImageModel> imageModels) {
        imageModels.forEach(s -> {
            insertImagebyUrl(s);
        });
    }

    public void updateImage(CmsBtFeedProductImageModel feedImage) {
        update("update_cms_bt_feed_product_image", feedImage);
    }
}
