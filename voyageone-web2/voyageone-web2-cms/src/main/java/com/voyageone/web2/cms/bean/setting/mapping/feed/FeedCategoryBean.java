package com.voyageone.web2.cms.bean.setting.mapping.feed;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedMappingModel;

import java.util.List;

/**
 * FeedMapping 画面特供数据模型,将画面需要的数据事先找好
 *
 * @author Jonas, 12/31/15.
 * @version 2.0.0
 * @since 2.0.0
 */
public class FeedCategoryBean {

    private int seq;

    private String path;

    private int isChild;

    private int level;

    private List<MappingBean> mappings;

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getIsChild() {
        return isChild;
    }

    public void setIsChild(int isChild) {
        this.isChild = isChild;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public List<MappingBean> getMappings() {
        return mappings;
    }

    public void setMappings(List<MappingBean> mappings) {
        this.mappings = mappings;
    }

    public static class MappingBean {

        private String _id;

        private int defaultMapping;

        private int defaultMain;

        private int matchOver;

        private String feedPath;

        private String mainPath;

        private MappingBean mainMapping;

        public MappingBean(CmsBtFeedMappingModel mapping, CmsBtFeedMappingModel mainMapping) {
            this._id = mapping.get_id();
            this.defaultMapping = mapping.getDefaultMapping();
            this.defaultMain = mapping.getDefaultMain();
            this.matchOver = mapping.getMatchOver();
            this.feedPath = mapping.getFeedCategoryPath();
            this.mainPath = mapping.getMainCategoryPath();
            if (mainMapping != null)
                this.mainMapping = new MappingBean(mainMapping, null);
        }

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
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

        public String getFeedPath() {
            return feedPath;
        }

        public void setFeedPath(String feedPath) {
            this.feedPath = feedPath;
        }

        public String getMainPath() {
            return mainPath;
        }

        public void setMainPath(String mainPath) {
            this.mainPath = mainPath;
        }

        public MappingBean getMainMapping() {
            return mainMapping;
        }

        public void setMainMapping(MappingBean mainMapping) {
            this.mainMapping = mainMapping;
        }
    }

    @JsonProperty
    public String getParentPath() {
        String path = this.path;
        int lastIndex = path.lastIndexOf("-");
        return lastIndex < 0 ? null : path.substring(0, lastIndex);
    }
}
