package com.voyageone.cms.service.model;

import com.voyageone.base.dao.mongodb.model.BaseMongoMap;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * {@link CmsMtFeedCategoryTreeModel} 的属性类型, 表示具体的类目
 * @author Jonas, 12/11/15
 * @version 2.0.0
 * @since 2.0.0
 */
public class CmsFeedCategoryModel extends BaseMongoMap<String, Object> {

//    private String path;
//
//    private String name;
//
//    private String cid;
//
//    private int isChild;
//
//    private List<CmsFeedMappingModel> mapping;
//
//    private List<CmsFeedCategoryModel> child;
//
//    private Map<String, List<String>> attribute;

    public String getPath() {
        return getAttribute("path");
    }

    public void setPath(String path) {
        setAttribute("path",path);
    }

    public String getName() {
        return getAttribute("name");
    }

    public void setName(String name) {
        setAttribute("name", name);
    }

    public String getCid() {
        return getAttribute("cid");
    }

    public void setCid(String cid) {
        setAttribute("cid", cid);
    }

    public int getIsChild() {
        return getAttribute("isChild");
    }

    public void setIsChild(int isChild) {
        setAttribute("isChild", isChild);
    }

    public List<CmsFeedMappingModel> getMapping() {
        List<CmsFeedMappingModel> mapping = getAttribute("mapping");
        if (mapping == null)
            mapping = new ArrayList<>();
        return mapping;
    }

    public void setMapping(List<CmsFeedMappingModel> mapping) {
        setAttribute("mapping", mapping);
    }

    public List<CmsFeedCategoryModel> getChild() {
        return getAttribute("child");
    }

    public void setChild(List<CmsFeedCategoryModel> child) {
        setAttribute("child", child);
    }

    public Map<String, List<String>> getAttribute() {
        return getAttribute("attribute");
    }

    public void setAttribute(Map<String, List<String>> attribute) {
        setAttribute("attribute", attribute);
    }
}
