package com.voyageone.cms.service.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.cms.service.model.CmsBtFeedProductImageModel;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by james.li on 2015/11/30.
 */
@Repository
public class CmsBtFeedProductImageDao extends BaseDao {

    public List<CmsBtFeedProductImageModel> selectImagebyUrl(CmsBtFeedProductImageModel feedImage) {
        return selectList("select_feed_image", feedImage);
    }

    public void updateImagebyUrl(CmsBtFeedProductImageModel feedImage) {
        insert("insert_feed_image", feedImage);
    }

    public void updateImagebyUrl(String channelId, List<String> url) {
        url.forEach(s -> {
            CmsBtFeedProductImageModel cmsBtFeedProductImageModel = new CmsBtFeedProductImageModel(channelId,s);
            updateImagebyUrl(cmsBtFeedProductImageModel);
        });
    }
}
