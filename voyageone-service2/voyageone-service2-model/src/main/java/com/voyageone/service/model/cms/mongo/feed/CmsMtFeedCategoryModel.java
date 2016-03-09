package com.voyageone.service.model.cms.mongo.feed;

import java.util.List;
import java.util.Map;

/**
 * {@link CmsMtFeedCategoryTreeModelx} 的属性类型, 表示具体的类目
 * @author Jonas, 12/11/15
 * @version 2.0.0
 * @since 2.0.0
 */
public class CmsMtFeedCategoryModel {

    private String path;

    private String name;

    private String cid;

    private int isChild;

    private List<CmsMtFeedCategoryModel> child;

    private Map<String, List<String>> attribute;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public int getIsChild() {
        return isChild;
    }

    public void setIsChild(int isChild) {
        this.isChild = isChild;
    }

    public List<CmsMtFeedCategoryModel> getChild() {
        return child;
    }

    public void setChild(List<CmsMtFeedCategoryModel> child) {
        this.child = child;
    }

    public Map<String, List<String>> getAttribute() {
        return attribute;
    }

    public void setAttribute(Map<String, List<String>> attribute) {
        this.attribute = attribute;
    }
}
