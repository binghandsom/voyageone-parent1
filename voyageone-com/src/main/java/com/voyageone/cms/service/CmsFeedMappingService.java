package com.voyageone.cms.service;

import com.voyageone.cms.service.dao.mongodb.CmsBtFeedMappingDao;
import com.voyageone.cms.service.model.CmsBtFeedMappingModel;
import com.voyageone.cms.service.model.CmsFeedCategoryModel;
import com.voyageone.cms.service.model.CmsFeedMappingModel;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Jonas, 12/10/15
 * @version 2.0.0
 * @since 2.0.0
 */
@Service("common.CmsFeedMappingService")
public class CmsFeedMappingService {

    @Autowired
    private CmsBtFeedMappingDao cmsBtFeedMappingDao;

    public class CategoryContext {

        private final CmsFeedCategoryModel feedCategoryModel;

        private final ChannelConfigEnums.Channel channel;

        public CategoryContext(CmsFeedCategoryModel feedCategoryModel, ChannelConfigEnums.Channel channel) {

            this.feedCategoryModel = feedCategoryModel;
            this.channel = channel;
        }

        public void removeMapping(CmsFeedMappingModel defaultMapping) {

            CmsBtFeedMappingModel feedMappingModel = cmsBtFeedMappingDao
                    .selectByKey(channel.getId(), feedCategoryModel.getPath(), defaultMapping.getMainCategoryPath());

            if (feedMappingModel == null)
                return;

            cmsBtFeedMappingDao.delete(feedMappingModel);
        }

        public void addMapping(CmsFeedMappingModel mapping, String userName) {

            CmsBtFeedMappingModel feedMappingModel = cmsBtFeedMappingDao
                    .selectByKey(channel.getId(), feedCategoryModel.getPath(), mapping.getMainCategoryPath());

            if (feedMappingModel == null) {
                feedMappingModel = new CmsBtFeedMappingModel();
                CmsBtFeedMappingModel.Scope scope = feedMappingModel.new Scope();
                scope.setChannelId(channel.getId());
                scope.setFeedCategoryPath(feedCategoryModel.getPath());
                feedMappingModel.setScope(scope);
                feedMappingModel.setCreater(userName);
            }

            feedMappingModel.getScope().setMainCategoryPath(mapping.getMainCategoryPath());
            // 这一步设定 Default 没有进行检查, 后续可考虑加入
            feedMappingModel.setDefaultMapping(mapping.getDefaultMapping());
            feedMappingModel.setDefaultMain(mapping.getDefaultMain());
            feedMappingModel.setModifier(userName);

            cmsBtFeedMappingDao.insert(feedMappingModel);
        }
    }
}
