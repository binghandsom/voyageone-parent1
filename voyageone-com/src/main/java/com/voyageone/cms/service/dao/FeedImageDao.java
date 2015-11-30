package com.voyageone.cms.service.dao;

import com.voyageone.base.dao.mongodb.BaseMongoDao;
import com.voyageone.cms.service.model.FeedImageModel;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by james.li on 2015/11/30.
 */
@Repository
public class FeedImageDao extends BaseMongoDao {

    public FeedImageDao() {
        super.entityClass = FeedImageModel.class;
    }

    public FeedImageModel selectImagebyUrl(String channelId, String url) {
        String query = "{\"url\":\"" + url + "\",\"channelId\":\""+channelId +"\"}";
        return mongoTemplate.findOne(query, FeedImageModel.class, collectionName);
    }

    public void updateImagebyUrl(String channelId, String url) {
        FeedImageModel feedImageModel = selectImagebyUrl(channelId, url);
        if(feedImageModel == null){
            feedImageModel = new FeedImageModel();
            feedImageModel.setChannelId(channelId);
            feedImageModel.setUrl(url);
        }
        feedImageModel.setStatus(0);
        mongoTemplate.save(feedImageModel,collectionName);
    }
    public void updateImagebyUrl(String channelId, List<String> url) {
        url.forEach(s -> updateImagebyUrl(channelId, s));
    }
}
