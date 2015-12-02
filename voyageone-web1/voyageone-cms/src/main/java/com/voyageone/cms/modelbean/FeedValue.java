package com.voyageone.cms.modelbean;

/**
 * 表 cms_mt_feed_attribute 的映射
 * <p>
 * Created by Jonas on 9/2/15.
 */
public class FeedValue {

    private String channel_id;

    private String attribute_name;

    private String attribute_value;

    private String create_on;

    public String getChannel_id() {
        return channel_id;
    }

    public void setChannel_id(String channel_id) {
        this.channel_id = channel_id;
    }

    public String getAttribute_name() {
        return attribute_name;
    }

    public void setAttribute_name(String attribute_name) {
        this.attribute_name = attribute_name;
    }

    public String getAttribute_value() {
        return attribute_value;
    }

    public void setAttribute_value(String attribute_value) {
        this.attribute_value = attribute_value;
    }

    public String getCreate_on() {
        return create_on;
    }

    public void setCreate_on(String create_on) {
        this.create_on = create_on;
    }
}
