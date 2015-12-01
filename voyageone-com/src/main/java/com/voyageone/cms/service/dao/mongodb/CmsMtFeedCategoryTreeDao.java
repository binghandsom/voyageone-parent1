package com.voyageone.cms.service.dao.mongodb;

import com.voyageone.base.dao.mongodb.BaseMongoDao;
import com.voyageone.cms.service.model.CmsMtFeedCategoryTreeModel;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by james.li on 2015/11/26.
 */
@Repository
public class CmsMtFeedCategoryTreeDao extends BaseMongoDao {

    public CmsMtFeedCategoryTreeDao() {
        super.entityClass = CmsMtFeedCategoryTreeModel.class;
    }

    public CmsMtFeedCategoryTreeModel selectFeedCategory(String channelId) {
        String query = "{\"channelId\":\"" + channelId + "\"}";
        return mongoTemplate.findOne(query, CmsMtFeedCategoryTreeModel.class, collectionName);
    }

    public void updateFeedCategory(CmsMtFeedCategoryTreeModel tree) {
//        CmsMtFeedCategoryTreeModel treeObject = selectFeedCategory(channelId);
//        if (treeObject == null) {
//            treeObject = new CmsMtFeedCategoryTreeModel();
//            treeObject.setChannelId(channelId);
//        }
//        treeObject.setCategoryTree(tree);
        mongoTemplate.save(tree, collectionName);
    }
}
