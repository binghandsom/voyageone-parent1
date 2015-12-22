package com.voyageone.web2.cms.bean.setting.mapping.feed;

import com.voyageone.web2.cms.views.setting.mapping.feed.CmsFeedMappingController;

/**
 * {@link CmsFeedMappingController } setMapping 的请求参数
 * @author Jonas, 12/22/15.
 * @version 2.0.0
 * @since 2.0.0
 */
public class SetMappingBean {

    private String from;

    private String to;

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
}
