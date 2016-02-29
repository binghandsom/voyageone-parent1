package com.voyageone.web2.cms.bean.setting.mapping.feed;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.voyageone.cms.service.model.CmsBtFeedMappingModel;
import com.voyageone.cms.service.model.CmsFeedCategoryModel;

/**
 * FeedMapping 画面特供数据模型,将画面需要的数据事先找好
 *
 * @author Jonas, 12/31/15.
 * @version 2.0.0
 * @since 2.0.0
 */
public class FeedCategoryBean {

    private int seq;

    private CmsFeedCategoryModel model;

    private int level;

    private CmsBtFeedMappingModel mapping;

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public CmsFeedCategoryModel getModel() {
        return model;
    }

    public void setModel(CmsFeedCategoryModel model) {
        this.model = model;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public CmsBtFeedMappingModel getMapping() {
        return mapping;
    }

    public void setMapping(CmsBtFeedMappingModel mapping) {
        this.mapping = mapping;
    }

    @JsonProperty
    public String getParentPath() {
        String path = model.getPath();
        int lastIndex = path.lastIndexOf("-");
        return lastIndex < 0 ? null : path.substring(0, lastIndex);
    }
}
