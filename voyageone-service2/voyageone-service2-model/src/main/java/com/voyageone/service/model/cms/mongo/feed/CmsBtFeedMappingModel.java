package com.voyageone.service.model.cms.mongo.feed;

import com.voyageone.base.dao.mongodb.model.ChannelPartitionModel;
import com.voyageone.service.model.cms.mongo.feed.mapping.Prop;

import java.util.List;

/**
 * Feed 数据到主数据映射关系定义
 *
 * @author zhujiaye 15/12/7.
 * @author Jonas 2015-12-09 14:36:37
 * @version 2.0.1
 * @since 2.0.0
 */
public class CmsBtFeedMappingModel extends ChannelPartitionModel {

    public CmsBtFeedMappingModel() {
    }

    public CmsBtFeedMappingModel(String channelId) {
        super(channelId);
    }

    private String feedCategoryPath;

    private String mainCategoryPath;

    private int defaultMapping;

    private int defaultMain;

    private int matchOver;

    private List<Prop> props;

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

    public int getDefaultMapping() {
        return defaultMapping;
    }

    public void setDefaultMapping(int defaultMapping) {
        this.defaultMapping = defaultMapping;
    }

    public int getDefaultMain() {
        return defaultMain;
    }

    public void setDefaultMain(int defaultMain) {
        this.defaultMain = defaultMain;
    }

    public int getMatchOver() {
        return matchOver;
    }

    public void setMatchOver(int matchOver) {
        this.matchOver = matchOver;
    }

    public List<Prop> getProps() {
        return props;
    }

    public void setProps(List<Prop> props) {
        this.props = props;
    }


}
