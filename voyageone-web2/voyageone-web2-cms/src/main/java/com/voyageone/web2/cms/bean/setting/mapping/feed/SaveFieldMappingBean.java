package com.voyageone.web2.cms.bean.setting.mapping.feed;

import com.voyageone.cms.service.model.feed.mapping.Prop;

import java.util.List;

/**
 * 保存某主类目的属性匹配时的请求参数
 *
 * @author Jonas, 12/28/15.
 * @version 2.0.0
 * @since 2.0.0
 */
public class SaveFieldMappingBean {

    private String feedCategoryPath;

    private String mainCategoryPath;

    private List<String> fieldPath;

    private Prop propMapping;

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

    public List<String> getFieldPath() {
        return fieldPath;
    }

    public void setFieldPath(List<String> fieldPath) {
        this.fieldPath = fieldPath;
    }

    public Prop getPropMapping() {
        return propMapping;
    }

    public void setPropMapping(Prop propMapping) {
        this.propMapping = propMapping;
    }
}
