package com.voyageone.task2.cms.bean;

public class ThirdPartyCategoryMappingBean {
    private String channelId;
    private String conditions;
    private int categoryId;
    private String categoryPath;

    public ThirdPartyCategoryMappingBean(
            String channelId,
            String conditions,
            int categoryId,
            String categoryPath
    ) {
        setChannelId(channelId);
        setConditions(conditions);
        setCategoryId(categoryId);
        setCategoryPath(categoryPath);
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getConditions() {
        return conditions;
    }

    public void setConditions(String conditions) {
        this.conditions = conditions;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryPath() {
        return categoryPath;
    }

    public void setCategoryPath(String categoryPath) {
        this.categoryPath = categoryPath;
    }
}
