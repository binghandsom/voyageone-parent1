package com.voyageone.service.impl.cms.feed;

import com.mongodb.WriteResult;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.configs.Enums.ChannelConfigEnums.Channel;
import com.voyageone.service.dao.cms.mongo.CmsBtFeedMappingDao;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedMappingModel;
import com.voyageone.service.model.cms.mongo.feed.CmsMtFeedCategoryTreeModel;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jonas, 12/10/15
 * @version 2.0.0
 * @since 2.0.0
 */
@Service("service.CmsFeedMappingService")
public class FeedMappingService extends BaseService {

    @Autowired
    private CmsBtFeedMappingDao feedMappingDao;

    @Autowired
    private FeedCategoryTreeService feedCategoryTreeService;

    public CmsBtFeedMappingModel getDefault(Channel channel, String feedCategory, boolean withProps) {
        return feedMappingDao.findDefault(channel.getId(), feedCategory, withProps);
    }

    public CmsBtFeedMappingModel getDefaultMain(Channel channel, String mainCategoryPath) {
        return feedMappingDao.findDefaultMainMapping(channel.getId(), mainCategoryPath);
    }

    public CmsBtFeedMappingModel getMapping(Channel channel, ObjectId objectId) {
        return feedMappingDao.findOne(objectId, channel.getId());
    }

    public WriteResult updateMapping(CmsBtFeedMappingModel feedMappingModel) {
        if (StringUtils.isEmpty(feedMappingModel.get_id()))
            feedMappingDao.insert(feedMappingModel);
        return feedMappingDao.update(feedMappingModel);
    }

    public List<CmsBtFeedMappingModel> getMappingWithoutProps(String selChannelId) {
        return feedMappingDao.findMappingWithoutProps(selChannelId);
    }

    public CmsBtFeedMappingModel getMappingWithoutProps(Channel channel, ObjectId mappingId) {
        return feedMappingDao.findOneWithoutProps(mappingId, channel.getId());
    }

    public List<CmsBtFeedMappingModel> getMappingsWithoutProps(String feedCategoryPath, String selChannelId) {
        return feedMappingDao.findMappingsWithoutProps(feedCategoryPath, selChannelId);
    }

    public List<CmsBtFeedMappingModel> getFeedMappings(String channelId) {
        return feedMappingDao.findMappingByChannelId(channelId);
    }

    /**
     * 为类目更新 Mapping
     *
     * @return 变动后的 Feed 类目的 Mapping 关系
     */
    public CmsBtFeedMappingModel setMapping(String feedCategoryPath, String mainCategoryPath, Channel channel, boolean replace) {

        if (StringUtils.isAnyEmpty(feedCategoryPath, mainCategoryPath))
            throw new BusinessException("木有参数");

        // 先检查 main mapping 是否存在
        CmsBtFeedMappingModel mainCategoryMapping =
                getMapping(channel, feedCategoryPath, mainCategoryPath);

        CmsBtFeedMappingModel defaultMapping = getDefault(channel, feedCategoryPath);

        // 如果有 main mapping
        if (mainCategoryMapping != null) {

            // 如果有 main mapping, 有 default mapping, 同时 main mapping 不是 default mapping。 则切换双方的 default 标识
            if (defaultMapping != null) {

                // 如果有 default mapping, 同时 main mapping 就是 default mapping, 则直接返回
                if (defaultMapping.get_id().equals(mainCategoryMapping.get_id()))
                    return defaultMapping;
                else
                    defaultMapping.setDefaultMapping(0);

            }

            // 如果没有 default mapping 则直接更新 main 为 default
            mainCategoryMapping.setDefaultMapping(1);

        } else {

            // 如果没有 main mapping 并且有 default mapping
            // 则如果 replace 为 true, 则替换 default mapping 的信息, 并清空 field mapping
            if (defaultMapping != null && replace) {

                defaultMapping.setMainCategoryPath(mainCategoryPath);
                defaultMapping.setMatchOver(0);
                defaultMapping.setProps(new ArrayList<>());

                mainCategoryMapping = defaultMapping;

            } else {

                // 如果 default mapping 也没有, 则创建新的 main mapping
                // 如果 replace 为 false, 则创建新的 main mapping
                mainCategoryMapping = new CmsBtFeedMappingModel();
                mainCategoryMapping.setChannelId(channel.getId());
                mainCategoryMapping.setFeedCategoryPath(feedCategoryPath);
                mainCategoryMapping.setMainCategoryPath(mainCategoryPath);
                mainCategoryMapping.setMatchOver(0);
                mainCategoryMapping.setDefaultMapping(1);

                if (defaultMapping != null)
                    defaultMapping.setDefaultMapping(0);

            }

        }

        // 最终检查 main mapping 是否可以作为 default main mapping

        CmsBtFeedMappingModel defaultMainMapping = getDefaultMain(channel, mainCategoryPath);

        boolean canBeDefaultMain = isCanBeDefaultMain(channel, feedCategoryPath);

        mainCategoryMapping.setDefaultMain(defaultMainMapping == null && canBeDefaultMain ? 1 : 0);

        // 更新或保存
        if (defaultMapping != null && defaultMapping != defaultMainMapping)
            updateMapping(defaultMapping);

        updateMapping(mainCategoryMapping);

        return mainCategoryMapping;
    }

    private CmsBtFeedMappingModel getMapping(Channel channel, String feedCategory, String mainCategoryPath) {
        return feedMappingDao.selectByKey(channel.getId(), feedCategory, mainCategoryPath);
    }

    private boolean isCanBeDefaultMain(Channel channel, String topCategoryPath) {

        CmsMtFeedCategoryTreeModel treeModel = feedCategoryTreeService.getCategoryNote(channel.getId(), topCategoryPath);

        return treeModel != null && treeModel.getIsParent() == 0;
    }

    private CmsBtFeedMappingModel getDefault(Channel channel, String feedCategory) {
        return getDefault(channel, feedCategory, true);
    }
}
