package com.voyageone.cms.service.model.feed.mapping;

import com.voyageone.cms.service.model.CmsBtFeedMappingModel;

/**
 * 表示 feed mapping 中,多组属性关系的作用范围. 参考 {@link CmsBtFeedMappingModel}
 *
 * @author Jonas, 12/28/15.
 * @version 2.0.0
 * @since 2.0.0
 */
public class Scope {

    private String channelId;

    private String feedCategoryPath;

    private String mainCategoryPath;

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getFeedCategoryPath() {
        return feedCategoryPath;
    }

    public void setFeedCategoryPath(String feedCategoryPath) {
        this.feedCategoryPath = feedCategoryPath;
    }

    public String getMainCategoryPath() {
        return mainCategoryPath;
    }

    public void setMainCategoryPath(String mainCategoryPath) {
        this.mainCategoryPath = mainCategoryPath;
    }
}
