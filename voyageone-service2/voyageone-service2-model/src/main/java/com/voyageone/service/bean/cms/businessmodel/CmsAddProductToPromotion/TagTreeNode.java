package com.voyageone.service.bean.cms.businessmodel.CmsAddProductToPromotion;
import java.util.List;
/**
 * Created by dell on 2016/11/1.
 */
public class TagTreeNode {
    int id;
    String name;
    int checked;//0:未选 1：半选 2全选
    int oldChecked;//0:未选 1：半选 2全选
    List<TagTreeNode> children;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getChecked() {
        return checked;
    }

    public void setChecked(int checked) {
        this.checked = checked;
    }

    public int getOldChecked() {
        return oldChecked;
    }

    public void setOldChecked(int oldChecked) {
        this.oldChecked = oldChecked;
    }

    public List<TagTreeNode> getChildren() {
        return children;
    }

    public void setChildren(List<TagTreeNode> children) {
        this.children = children;
    }
}
