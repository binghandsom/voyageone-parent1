package com.voyageone.cms.service.model;

import com.voyageone.base.dao.mongodb.model.ChannelPartitionModel;
import com.voyageone.cms.feed.Condition;

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

    public class Scope {
        private String channelId;
        private String feedCategoryPath;
        private String mainCategoryId;

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

        public String getMainCategoryId() {
            return mainCategoryId;
        }

        public void setMainCategoryId(String mainCategoryId) {
            this.mainCategoryId = mainCategoryId;
        }
    }

    public class Prop {

        private String prop;
        private Mapping mappings;
        private List<Prop> children;

        public String getProp() {
            return prop;
        }

        public void setProp(String prop) {
            this.prop = prop;
        }

        public Mapping getMappings() {
            return mappings;
        }

        public void setMappings(Mapping mappings) {
            this.mappings = mappings;
        }

        public List<Prop> getChildren() {
            return children;
        }

        public void setChildren(List<Prop> children) {
            this.children = children;
        }
    }

    public class Mapping {

        private List<Condition> condition;
        private SrcType type;
        private String val;

        public List<Condition> getCondition() {
            return condition;
        }

        public void setCondition(List<Condition> condition) {
            this.condition = condition;
        }

        public SrcType getType() {
            return type;
        }

        public void setType(SrcType type) {
            this.type = type;
        }

        public String getVal() {
            return val;
        }

        public void setVal(String val) {
            this.val = val;
        }
    }

    public enum SrcType {
        text,
        propFeed,
        propMain,
        optionMain,
        optionPlatform,
        dict
    }
}
