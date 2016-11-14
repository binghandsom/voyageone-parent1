package com.voyageone.service.bean.cms.businessmodel.PromotionProduct;

public class ProductTagInfo {
    int tagId;
    String tagName;
    int checked;//0 删除  2增加

    public int getChecked() {
        return checked;
    }

    public void setChecked(int checked) {
        this.checked = checked;
    }

    public int getTagId() {
        return tagId;
    }

    public void setTagId(int tagId) {
        this.tagId = tagId;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }
}