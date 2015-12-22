package com.voyageone.cms.service.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * {@link CmsMtFeedCategoryTreeModel} 的属性类型, 表示具体的类目
 * @author Jonas, 12/11/15
 * @version 2.0.0
 * @since 2.0.0
 */
public class CmsFeedCategoryModel {

    private String path;

    private String name;

    private String cid;

    private int isChild;

    private List<CmsFeedMappingModel> mapping;

    private List<CmsFeedCategoryModel> child;

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

    public List<CmsFeedMappingModel> getMapping() {
        if (mapping == null)
            mapping = new ArrayList<>();
        return mapping;
    }

    public void setMapping(List<CmsFeedMappingModel> mapping) {
        this.mapping = mapping;
    }

    public List<CmsFeedCategoryModel> getChild() {
        return child;
    }

    public void setChild(List<CmsFeedCategoryModel> child) {
        this.child = child;
    }

    public Map<String, List<String>> getAttribute() {
        return attribute;
    }

    public void setAttribute(Map<String, List<String>> attribute) {
        this.attribute = attribute;
    }
}
