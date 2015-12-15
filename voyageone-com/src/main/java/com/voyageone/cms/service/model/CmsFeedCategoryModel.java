package com.voyageone.cms.service.model;

import com.voyageone.base.dao.mongodb.model.BaseMongoMap;

import java.util.List;
import java.util.Map;

/**
 * {@link CmsMtFeedCategoryTreeModel} 的属性类型, 表示具体的类目
 * @author Jonas, 12/11/15
 * @version 2.0.0
 * @since 2.0.0
 */
public class CmsFeedCategoryModel extends BaseMongoMap<String, Object> {

    private final static String PATH = "path";

    private final static String NAME = "name";

    private final static String CID = "cid";

    private final static String IS_CHILD = "isChild";

    private final static String MAPPING = "mapping";

    private final static String CHILD = "child";

    private final static String ATTRIBUTE = "attribute";

    public String getPath() {
        return getAttribute(PATH);
    }

    public void setPath(String path) {
        setAttribute(PATH, path);
    }

    public String getName() {
        return getAttribute(NAME);
    }

    public void setName(String name) {
        setAttribute(NAME, name);
    }

    public String getCid() {
        return getAttribute(CID);
    }

    public void setCid(String cid) {
        setAttribute(CID, cid);
    }

    public int getIsChild() {
        return getAttribute(IS_CHILD);
    }

    public void setIsChild(int isChild) {
        setAttribute(IS_CHILD, isChild);
    }

    public List<CmsFeedMappingModel> getMapping() {
        return getAttribute(MAPPING);
    }

    public void setMapping(List<CmsFeedMappingModel> mapping) {
        setAttribute(MAPPING, mapping);
    }

    public List<CmsFeedCategoryModel> getChild() {
        return getAttribute(CHILD);
    }

    public void setChild(List<CmsFeedCategoryModel> child) {
        setAttribute(CHILD, child);
    }

    public Map<String, List<String>> getAttribute() {
        return getAttribute(ATTRIBUTE);
    }

    public void setAttribute(Map<String, List<String>> attribute) {
        setAttribute(ATTRIBUTE, attribute);
    }
}
