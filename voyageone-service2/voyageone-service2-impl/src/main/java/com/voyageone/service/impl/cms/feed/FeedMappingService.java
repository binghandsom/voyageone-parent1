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

    private CmsBtFeedMappingModel getDefault(Channel channel, String feedCategory) {
        return getDefault(channel, feedCategory, true);
    }

    public CmsBtFeedMappingModel getDefault(Channel channel, String feedCategory, boolean withProps) {
        return feedMappingDao.findDefault(channel.getId(), feedCategory, withProps);
    }

    public CmsBtFeedMappingModel getDefaultMain(Channel channel, String mainCategoryPath) {
        return feedMappingDao.findDefaultMainMapping(channel.getId(), mainCategoryPath);
    }

    private CmsBtFeedMappingModel getMapping(Channel channel, String feedCategory, String mainCategoryPath) {
        return feedMappingDao.selectByKey(channel.getId(), feedCategory, mainCategoryPath);
    }

    public CmsBtFeedMappingModel getMapping(Channel channel, ObjectId objectId) {
        return feedMappingDao.findOne(objectId, channel.getId());
    }

    public WriteResult updateMapping(CmsBtFeedMappingModel feedMappingModel) {
        return feedMappingDao.update(feedMappingModel);
    }

    public List<CmsBtFeedMappingModel> getMappingWithoutProps(String selChannelId) {
        return feedMappingDao.findMappingWithoutProps(selChannelId);
    }

    private boolean isCanBeDefaultMain(Channel channel, String topCategoryPath) {

        CmsMtFeedCategoryTreeModel treeModel = feedCategoryTreeService.getCategoryNote(channel.getId(), topCategoryPath);

        return treeModel != null && treeModel.getIsParent() == 0;
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
    public CmsBtFeedMappingModel setMapping(String feedCategoryPath, String mainCategoryPath, Channel channel) {

        if (StringUtils.isAnyEmpty(feedCategoryPath, mainCategoryPath))
            throw new BusinessException("木有参数");

        // 尝试查询 Mapping

        CmsBtFeedMappingModel defaultMapping = getDefault(channel, feedCategoryPath);

        CmsBtFeedMappingModel defaultMainMapping = getDefaultMain(channel, mainCategoryPath);

        boolean canBeDefaultMain = isCanBeDefaultMain(channel, feedCategoryPath);

        // 如果当前 Feed 类目已经有了默认的 Mapping
        // 如果当前的默认 Mapping 就是这次给的主类目的话, 就不用继续了
        // 如果不是当前的主类目, 就清空属性的 Mapping, 更换主类目路径
        if (defaultMapping != null) {

            if (!defaultMapping.getMainCategoryPath().equals(mainCategoryPath)) {
                defaultMapping.setMainCategoryPath(mainCategoryPath);
                defaultMapping.setDefaultMain(defaultMainMapping == null && canBeDefaultMain ? 1 : 0);
                defaultMapping.setMatchOver(0);
                defaultMapping.setProps(new ArrayList<>());

                updateMapping(defaultMapping);
            }

            return defaultMapping;
        }

        // 如果当前 Feed 类目已经 Mapping 到这个主类目, 只是不是默认 Mapping, 则只更新默认标识位
        CmsBtFeedMappingModel mainCategoryMapping =
                getMapping(channel, feedCategoryPath, mainCategoryPath);

        if (mainCategoryMapping != null) {
            mainCategoryMapping.setDefaultMapping(1);
            updateMapping(defaultMainMapping);

            return mainCategoryMapping;
        }

        // 如果上面的全部不成立, 就需要全新创建 Mapping

        defaultMapping = new CmsBtFeedMappingModel();

        defaultMapping.setChannelId(channel.getId());
        defaultMapping.setFeedCategoryPath(feedCategoryPath);
        defaultMapping.setMainCategoryPath(mainCategoryPath);
        defaultMapping.setMatchOver(0);
        defaultMapping.setDefaultMapping(1);
        defaultMapping.setDefaultMain(defaultMainMapping == null && canBeDefaultMain ? 1 : 0);
        defaultMapping.setChannelId(channel.getId());
        updateMapping(defaultMapping);

        return defaultMapping;
    }
}
