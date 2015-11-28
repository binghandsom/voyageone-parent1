package com.voyageone.cms.service.dao;

import com.voyageone.base.dao.mongodb.BaseJomgoTemplate;
import com.voyageone.cms.service.model.FeedCategoryModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by james.li on 2015/11/26.
 */
@Repository
public class FeedCategoryDao {
    @Autowired
    BaseJomgoTemplate mongoTemplate;

    public FeedCategoryModel getFeedCategory(String channelId) {
        String query = "{\"channelId\":\"" + channelId + "\"}";
        return mongoTemplate.findOne(query, FeedCategoryModel.class, FeedCategoryModel.COLLECTION_NAME);
    }

    public void setFeedCategory(String channelId, List<Map> tree) {
        FeedCategoryModel treeObject = getFeedCategory(channelId);
        if (treeObject == null) {
            treeObject = new FeedCategoryModel();
            treeObject.setChannelId(channelId);
        }
        treeObject.setCategoryTree(tree);
        mongoTemplate.save(treeObject, FeedCategoryModel.COLLECTION_NAME);
    }
}
