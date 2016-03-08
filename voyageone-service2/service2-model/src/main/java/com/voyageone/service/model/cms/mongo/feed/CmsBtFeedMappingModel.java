package com.voyageone.service.model.cms.mongo.feed;

import com.voyageone.base.dao.mongodb.model.ChannelPartitionModel;
import com.voyageone.service.model.cms.mongo.feed.mapping.Prop;
import com.voyageone.service.model.cms.mongo.feed.mapping.Scope;

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

    private Scope scope;

    private int defaultMapping;

    private int defaultMain;

    private int matchOver;

    private List<Prop> props;

    public Scope getScope() {
        return scope;
    }

    public void setScope(Scope scope) {
        this.scope = scope;
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
