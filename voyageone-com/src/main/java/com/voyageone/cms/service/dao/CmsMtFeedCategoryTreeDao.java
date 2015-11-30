package com.voyageone.cms.service.dao;

import com.voyageone.base.dao.mongodb.BaseJomgoTemplate;
import com.voyageone.cms.service.model.CmsMtFeedCategoryTreeModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by james.li on 2015/11/26.
 */
@Repository
public class CmsMtFeedCategoryTreeDao {
    @Autowired
    BaseJomgoTemplate mongoTemplate;

    public CmsMtFeedCategoryTreeModel selectFeedCategory(String channelId) {
        String query = "{\"channelId\":\"" + channelId + "\"}";
        return mongoTemplate.findOne(query, CmsMtFeedCategoryTreeModel.class, CmsMtFeedCategoryTreeModel.COLLECTION_NAME);
    }

    public void updateFeedCategory(String channelId, List<Map> tree) {
        CmsMtFeedCategoryTreeModel treeObject = selectFeedCategory(channelId);
        if (treeObject == null) {
            treeObject = new CmsMtFeedCategoryTreeModel();
            treeObject.setChannelId(channelId);
        }
        treeObject.setCategoryTree(tree);
        mongoTemplate.save(treeObject, CmsMtFeedCategoryTreeModel.COLLECTION_NAME);
    }
}
