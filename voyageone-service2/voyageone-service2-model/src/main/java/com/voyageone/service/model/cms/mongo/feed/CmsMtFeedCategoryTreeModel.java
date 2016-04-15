package com.voyageone.service.model.cms.mongo.feed;

import com.voyageone.base.dao.mongodb.model.BaseMongoModel;
import com.voyageone.base.dao.mongodb.model.ChannelPartitionModel;

import java.util.List;
import java.util.Map;

/**
 * cms_mt_feed_category_tree 的弱类型模型.
 * 如果需要强类型模型,请参看{@link CmsMtFeedCategoryTreeModelx}
 *
 * @author james.li, 2015/11/26.
 * @author Jonas, 2015-12-11 19:17:18
 * @version 2.0.0
 * @since 2.0.0
 */
public class CmsMtFeedCategoryTreeModel extends ChannelPartitionModel {

    private String catId;
    private String catName;
    private String catPath;
    private String parentCatId;
    private Integer isParent;
    private List<CmsMtFeedCategoryTreeModel> children;

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public String getCatPath() {
        return catPath;
    }

    public void setCatPath(String catPath) {
        this.catPath = catPath;
    }

    public String getParentCatId() {
        return parentCatId;
    }

    public void setParentCatId(String parentCatId) {
        this.parentCatId = parentCatId;
    }

    public Integer getIsParent() {
        return isParent;
    }

    public void setIsParent(Integer isParent) {
        this.isParent = isParent;
    }

    public List<CmsMtFeedCategoryTreeModel> getChildren() {
        return children;
    }

    public void setChildren(List<CmsMtFeedCategoryTreeModel> children) {
        this.children = children;
    }
}
