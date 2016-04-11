package com.voyageone.service.impl.cms.feed;

import com.voyageone.service.dao.cms.mongo.CmsMtFeedCategoryTreeDao;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.cms.mongo.feed.CmsMtFeedCategoryModel;
import com.voyageone.service.model.cms.mongo.feed.CmsMtFeedCategoryTreeModelx;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by DELL on 2016/4/5.
 */

@Service
public class FeedCategoryTreeService extends BaseService {

    @Autowired
    private CmsMtFeedCategoryTreeDao cmsMtFeedCategoryTreeDao;

    // 取得Top类目路径数据
    public CmsMtFeedCategoryTreeModelx getFeedCategory(String channelId) {
        return cmsMtFeedCategoryTreeDao.selectFeedCategoryx(channelId);
    }

    // 取得Top类目路径数据
    public List<CmsMtFeedCategoryModel> getTopFeedCategories(String channelId) {
        CmsMtFeedCategoryTreeModelx treeModelx = getFeedCategory(channelId);
        return treeModelx.getCategoryTree();
    }

    public List<CmsMtFeedCategoryModel> getOnlyTopFeedCategories(String channelId) {
        CmsMtFeedCategoryTreeModelx treeModelx = cmsMtFeedCategoryTreeDao.selectTopCategories(channelId);
        return treeModelx.getCategoryTree();
    }

    public CmsMtFeedCategoryTreeModelx getFeedCategory(String channelId, String categoryId) {
        return cmsMtFeedCategoryTreeDao.selectTopCategory(channelId, categoryId);
    }

}
