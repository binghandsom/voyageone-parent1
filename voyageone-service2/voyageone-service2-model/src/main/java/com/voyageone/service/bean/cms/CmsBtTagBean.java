package com.voyageone.service.bean.cms;

import com.voyageone.common.util.JsonUtil;
import com.voyageone.service.model.cms.CmsBtTagModel;

import java.util.List;

/**
 * Created by gjl on 2016/4/27.
 */
public class CmsBtTagBean extends CmsBtTagModel {

    private List<CmsBtTagBean> children;
    private Boolean isLeaf;
    private String tagChildrenName;

    public List<CmsBtTagBean> getChildren() {
        return children;
    }

    public void setChildren(List<CmsBtTagBean> children) {
        this.children = children;
    }

    public Boolean getIsLeaf() {
        return isLeaf;
    }

    public void setIsLeaf(Boolean isLeaf) {
        this.isLeaf = isLeaf;
    }

    public String getTagChildrenName() {
        return tagChildrenName;
    }

    public void setTagChildrenName(String tagChildrenName) {
        this.tagChildrenName = tagChildrenName;
    }

    @Override
    public String toString() {
        return JsonUtil.bean2Json(this);
    }

    @Override
    public boolean equals(Object cbb) {
        if (!(cbb instanceof CmsBtTagBean) || cbb == null) {
            return false;
        }
        if (this.id == null) {
            return false;
        }
        return this.id.equals(((CmsBtTagBean) cbb).getId());
    }
}
