package com.voyageone.task2.cms.bean;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 对应 BCBG 的 style json 文件
 * Created by Jonas on 10/16/15.
 */
public class BcbgStyleBean {

    private String styleID;

    private String productDesc;

    private List<String> productImgURLs;

    private String md5;

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

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
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

    /**
     * 该方法用于从数据库取回 Style 数据时使用, MyBatis 调用.
     * @param imageUrls ;; 双分分隔的图片地址
     */
    public void setImageUrls(String imageUrls) {

        List<String> imageList = new ArrayList<>();

        String[] imageArr = imageUrls.split(";;");

        Collections.addAll(imageList, imageArr);

        setProductImgURLs(imageList);
    }
}
