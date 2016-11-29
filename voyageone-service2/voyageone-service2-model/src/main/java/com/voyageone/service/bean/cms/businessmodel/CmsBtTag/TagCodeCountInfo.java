package com.voyageone.service.bean.cms.businessmodel.CmsBtTag;

/**
 * Created by dell on 2016/11/3.
 */
public class TagCodeCountInfo {
    int id;
    String tagName;
    int productCount;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public int getProductCount() {
        return productCount;
    }

    public void setProductCount(int productCount) {
        this.productCount = productCount;
    }
}
