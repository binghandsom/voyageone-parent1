package com.voyageone.batch.cms.bean;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * 对应 BCBG 的 style json 文件
 * Created by Jonas on 10/16/15.
 */
public class BcbgStyleBean {

    private String styleID;

    private String productDesc;

    private List<String> productImgURLs;

    private Boolean valid;

    public String getStyleID() {
        return styleID;
    }

    public void setStyleID(String styleID) {
        this.styleID = styleID;
    }

    public String getProductDesc() {
        return productDesc;
    }

    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc;
    }

    public List<String> getProductImgURLs() {
        return productImgURLs;
    }

    public void setProductImgURLs(List<String> productImgURLs) {
        this.productImgURLs = productImgURLs;
    }

    /**
     * 获取数据有效性. !! 实际校验只运行一次.因为导入到程序的数据,程序不会修改任何内容,而校验会进行多次.为了节省进行缓存
     */
    public boolean isValid() {

        if (valid != null) return valid;

        boolean hasStringEmpty = StringUtils.isAnyEmpty(getStyleID(), getProductDesc());

        if (hasStringEmpty) return (valid = false);

        return (valid = getProductImgURLs() != null && getProductImgURLs().size() > 0);
    }
}
