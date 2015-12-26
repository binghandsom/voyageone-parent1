package com.voyageone.web2.cms.bean.setting.mapping.feed;

/**
 * 获取具体到字段的 Mapping 设定的请求参数
 *
 * @author Jonas, 12/26/15.
 * @version 2.0.0
 * @since 2.0.0
 */
public class GetFieldMappingBean {

    private String feedCategoryPath;

    private String mainCategoryPath;

    private String field;

    public String getFeedCategoryPath() {
        return feedCategoryPath;
    }

    public void setFeedCategoryPath(String feedCategoryPath) {
        this.feedCategoryPath = feedCategoryPath;
    }

    public String getMainCategoryPath() {
        return mainCategoryPath;
    }

    public void setMainCategoryPath(String mainCategoryPath) {
        this.mainCategoryPath = mainCategoryPath;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }
}
