package com.voyageone.cms.service.dao;

import com.voyageone.base.dao.mongodb.BaseJomgoTemplate;
import com.voyageone.cms.service.bean.FeedCategoryBean;
import com.voyageone.cms.service.bean.FeedCategoryChildBean;
import com.voyageone.common.components.channelAdvisor.bean.orders.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by james.li on 2015/11/26.
 */
@Repository
public class FeedToCmsDao  {
    @Autowired
    BaseJomgoTemplate mongoTemplate;

    public FeedCategoryBean getFeedCategory(String channelId){
        String query = "{\"channelId\":\"" + channelId+"\"}";
        return mongoTemplate.findOne(query, FeedCategoryBean.class, "feed_category_info");
    }

    public void setFeedCategory(String channelId, List<FeedCategoryChildBean> tree){
        FeedCategoryBean treeObject = getFeedCategory(channelId);
        if(treeObject == null){
            treeObject = new FeedCategoryBean();
            treeObject.setChannelId(channelId);
        }
        treeObject.setCategoryTree(tree);
        mongoTemplate.save(treeObject,"feed_category_info");
    }
}
