package com.voyageone.web2.cms.bean.setting.mapping.feed;

import com.voyageone.web2.cms.views.mapping.feed.CmsFeedMappingController;

/**
 * {@link CmsFeedMappingController } setMapping 的请求参数
 * @author Jonas, 12/22/15.
 * @version 2.0.0
 * @since 2.0.0
 */
public class SetMappingBean {

    private String from;

    private String to;

    private boolean isCommon;

    private String mappingId;

    public SetMappingBean() {
    }

    public SetMappingBean(String from, String to) {
        this.from = from;
        this.to = to;
    }

    public String getMappingId() {
        return mappingId;
    }

    public void setMappingId(String mappingId) {
        this.mappingId = mappingId;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public boolean isCommon() {
        return isCommon;
    }

    public void setIsCommon(boolean common) {
        isCommon = common;
    }
}
