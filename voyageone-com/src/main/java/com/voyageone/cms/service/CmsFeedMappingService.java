package com.voyageone.cms.service;

import com.voyageone.cms.service.dao.mongodb.CmsBtFeedMappingDao;
import com.voyageone.cms.service.model.CmsBtFeedMappingModel;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;
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

    public List<CmsBtFeedMappingModel> getFeedMapping(ChannelConfigEnums.Channel channel) {
        return cmsBtFeedMappingDao.selectCategoryMappingByChannel(channel.getId());
    }
}
