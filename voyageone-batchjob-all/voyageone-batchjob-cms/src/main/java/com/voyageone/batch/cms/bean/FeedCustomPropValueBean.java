package com.voyageone.batch.cms.bean;

/**
 * Created by zhujiaye on 16/1/29.
 */
public class FeedCustomPropValueBean {
    private String channel_id;
    private String feed_cat_path;
    private String feed_prop_origin;
    private String feed_prop_translate;

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

    public String getFeed_prop_origin() {
        return feed_prop_origin;
    }

    public void setFeed_prop_origin(String feed_prop_origin) {
        this.feed_prop_origin = feed_prop_origin;
    }

    public String getFeed_prop_translate() {
        return feed_prop_translate;
    }

    public void setFeed_prop_translate(String feed_prop_translate) {
        this.feed_prop_translate = feed_prop_translate;
    }
}
