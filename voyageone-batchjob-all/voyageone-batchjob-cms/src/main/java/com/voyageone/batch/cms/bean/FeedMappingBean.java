package com.voyageone.batch.cms.bean;

/**
 * MainPropTodoListBean
 * Created by tom on 7/28/2015.
 *
 * @author tom
 */
public class FeedMappingBean {
	private String channel_id;
	private String main_category_id;
	private String prop_id;
	private String conditions;
	private String type;
	private String value;

    public String getChannel_id() {
        return channel_id;
    }

    public void setChannel_id(String channel_id) {
        this.channel_id = channel_id;
    }

    public String getMain_category_id() {
        return main_category_id;
    }

    public void setMain_category_id(String main_category_id) {
        this.main_category_id = main_category_id;
    }

    public String getProp_id() {
        return prop_id;
    }

    public void setProp_id(String prop_id) {
        this.prop_id = prop_id;
    }

    public String getConditions() {
        return conditions;
    }

    public void setConditions(String conditions) {
        this.conditions = conditions;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
