package com.voyageone.cms.service;

import com.mongodb.WriteResult;
import com.voyageone.cms.service.dao.mongodb.CmsBtFeedMappingDao;
import com.voyageone.cms.service.model.CmsBtFeedMappingModel;
import com.voyageone.common.configs.Enums.ChannelConfigEnums.Channel;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Jonas, 12/10/15
 * @version 2.0.0
 * @since 2.0.0
 */
@Service("common.CmsFeedMappingService")
public class CmsFeedMappingService {

    @Autowired
    private CmsBtFeedMappingDao cmsBtFeedMappingDao;

    public CmsBtFeedMappingModel getDefault(Channel channel, String feedCategory) {
        return getDefault(channel, feedCategory, true);
    }

    public CmsBtFeedMappingModel getDefault(Channel channel, String feedCategory, boolean withProps) {
        return cmsBtFeedMappingDao.findDefault(channel.getId(), feedCategory, withProps);
    }

    public CmsBtFeedMappingModel getDefaultMain(Channel channel, String mainCategoryPath) {
        return cmsBtFeedMappingDao.findDefaultMainMapping(channel.getId(), mainCategoryPath);
    }

    public CmsBtFeedMappingModel getMapping(Channel channel, String feedCategory, String mainCategoryPath) {
        return cmsBtFeedMappingDao.selectByKey(channel.getId(), feedCategory, mainCategoryPath);
    }

    public CmsBtFeedMappingModel getMapping(ObjectId objectId) {
        return cmsBtFeedMappingDao.findOne(objectId);
    }

    public WriteResult setMapping(CmsBtFeedMappingModel feedMappingModel) {
        return cmsBtFeedMappingDao.update(feedMappingModel);
    }

    public List<CmsBtFeedMappingModel> getMappingWithoutProps(String selChannelId, String topCategoryPath) {
        return cmsBtFeedMappingDao.findMappingWithoutProps(selChannelId, topCategoryPath);
    }
}
