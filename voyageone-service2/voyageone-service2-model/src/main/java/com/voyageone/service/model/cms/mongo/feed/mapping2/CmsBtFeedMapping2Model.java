package com.voyageone.service.model.cms.mongo.feed.mapping2;

import com.voyageone.base.dao.mongodb.model.ChannelPartitionModel;

/**
 * Feed 到主数据的类目匹配关系
 * Created by jonas on 16/6/6.
 *
 * @version 2.0.0
 * @since 2.0.0
 */
public class CmsBtFeedMapping2Model extends ChannelPartitionModel {

    public CmsBtFeedMapping2Model() {
    }

    public CmsBtFeedMapping2Model(String channelId) {
        super(channelId);
    }

    private String feedCategoryPath;

    private String mainCategoryPath;

    private int defaultMapping;

    public int getDefaultMapping() {
        return defaultMapping;
    }

    public void setDefaultMapping(int defaultMapping) {
        this.defaultMapping = defaultMapping;
    }

    public String getMainCategoryPath() {
        return mainCategoryPath;
    }

    public void setMainCategoryPath(String mainCategoryPath) {
        this.mainCategoryPath = mainCategoryPath;
    }

    public String getFeedCategoryPath() {
        return feedCategoryPath;
    }

    public void setFeedCategoryPath(String feedCategoryPath) {
        this.feedCategoryPath = feedCategoryPath;
    }
}
