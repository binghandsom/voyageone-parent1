package com.voyageone.web2.cms.views.channel;

import com.voyageone.cms.service.dao.mongodb.CmsMtFeedCategoryTreeDao;
import com.voyageone.cms.service.model.CmsFeedCategoryModel;
import com.voyageone.cms.service.model.CmsMtFeedCategoryTreeModelx;
import com.voyageone.web2.base.BaseAppService;
import com.voyageone.web2.core.bean.UserSessionBean;
import com.voyageone.web2.cms.dao.CmsBtFeedCustomPropDao;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author jiang, 2016/2/26
 * @version 2.0.0从
 */
@Service
public class CmsFeedCustPropService extends BaseAppService {

    @Autowired
    private CmsMtFeedCategoryTreeDao cmsMtFeedCategoryTreeDao;
    @Autowired
    private CmsBtFeedCustomPropDao cmsBtFeedCustomPropDao;
    @Autowired
    private MongoTemplate mongoTemplate;

    // 取得类目路径数据(树型结构)
    public List<CmsFeedCategoryModel> getTopCategories(UserSessionBean user) {
        CmsMtFeedCategoryTreeModelx treeModelx = cmsMtFeedCategoryTreeDao.findFeedCategoryx(user.getSelChannelId());
        return treeModelx.getCategoryTree();
    }

    // 根据类目路径查询自定义已翻译属性信息
    public List<Map<String, Object>> selectTransProp(String channelId, String catPath) {
        Map<String, Object> params = new HashMap<String, Object>(2);
        params.put("channelId", channelId);
        params.put("feedCatPath", catPath);
        return cmsBtFeedCustomPropDao.selectTransProp(params);
    }

    // 根据类目路径查询自定义未翻译属性信息
    public List<Map<String, Object>> selectOrigProp(String channelId, String catPath) {
        Map<String, Object> params = new HashMap<String, Object>(2);
        params.put("channelId", channelId);
        params.put("feedCatPath", catPath);
        return cmsBtFeedCustomPropDao.selectOrigProp(params);
    }

    // 取得全店铺共通配置属性
    public String getSameAttr(String channelId) {
        Map<String, Object> params = new HashMap<String, Object>(2);
        params.put("channelId", channelId);
        String rslt = cmsBtFeedCustomPropDao.getSameAttr(params);
        return rslt;
    }

    // 根据类目路径查询自定义未翻译属性信息(不包含共通属性)
    public List<Object> selectCatAttr(String channelId) {
        Query query = new Query();
        Criteria criteria = Criteria.where("channelId").is(channelId);
        query.addCriteria(criteria);
        return mongoTemplate.find(query, Object.class, "cms_mt_feed_category_tree");
    }
}
