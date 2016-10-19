package com.voyageone.service.dao.cms.mongo;

import com.voyageone.base.dao.mongodb.BaseMongoChannelDao;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel;
import com.voyageone.service.model.cms.mongo.jm.promotion.CmsBtJmPromotionImagesModel;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 聚美专场图片 DAO
 *
 * Created by jonas on 2016/10/14.
 *
 * @author jonas
 * @version 2.8.0
 * @since 2.8.0
 */
@Repository
public class CmsBtJmPromotionImagesDao extends BaseMongoChannelDao<CmsBtJmPromotionImagesModel> {

    /**
     * 存储聚美活动图片信息
     * @param model: 聚美活动图片模型
     */
    public void saveJmPromotionImages(CmsBtJmPromotionImagesModel model){
        mongoTemplate.save(model);
    }

    public List<CmsBtJmPromotionImagesModel> selectJmPromotionImagesList(String channelId,int promotionId,int jmPromotionId) {
        String query = "{\"promotionId\":" + promotionId + ",\"jmPromotionId\":" + jmPromotionId + "}";
        return select(query, channelId);
    }
}
