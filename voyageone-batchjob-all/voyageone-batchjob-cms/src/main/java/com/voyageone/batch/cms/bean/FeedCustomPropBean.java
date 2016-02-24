package com.voyageone.batch.cms.bean;

import com.voyageone.common.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhujiaye on 16/1/29.
 */
public class FeedCustomPropBean {
    private String channel_id;
    private String feed_cat_path;
    private String feed_prop;
    private String feed_prop_translate;
    private String display_order;

    public String getChannel_id() {
        return channel_id;
    }

    public void setChannel_id(String channel_id) {
        this.channel_id = channel_id;
    }

    public String getFeed_cat_path() {
        return feed_cat_path;
    }

    public void setFeed_cat_path(String feed_cat_path) {
        this.feed_cat_path = feed_cat_path;
    }

    public String getFeed_prop() {
        return feed_prop;
    }

    public void setFeed_prop(String feed_prop) {
        this.feed_prop = feed_prop;
    }

    public String getFeed_prop_translate() {
        return feed_prop_translate;
    }

    public void setFeed_prop_translate(String feed_prop_translate) {
        this.feed_prop_translate = feed_prop_translate;
    }

    public String getDisplay_order() {
        return display_order;
    }

    public void setDisplay_order(String display_order) {
        this.display_order = display_order;
    }
}
