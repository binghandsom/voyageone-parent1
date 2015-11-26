package com.voyageone.cms.service.bean;

import java.util.List;

/**
 * Created by james.li on 2015/11/26.
 */
public class FeedCategoryChildBean {
    private String category;
    private List<FeedCategoryChildBean> child;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<FeedCategoryChildBean> getChild() {
        return child;
    }

    public void setChild(List<FeedCategoryChildBean> child) {
        this.child = child;
    }
}
