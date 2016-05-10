package com.voyageone.service.bean.cms;

import com.voyageone.service.model.cms.CmsBtTagModel;
import com.voyageone.service.model.cms.mongo.channel.CmsBtImageGroupModel;

import java.util.List;

/**
 * Created by gjl on 2016/4/27.
 */
public class CmsBtImageGroupBean extends CmsBtImageGroupModel {

    private String viewTypeName;
    private String imageTypeName;
    private String cartName;
    private List<String> brandNameTrans;
    private List<String> productTypeTrans;
    private List<String> sizeTypeTrans;

    public String getViewTypeName() {
        return viewTypeName;
    }

    public void setViewTypeName(String viewTypeName) {
        this.viewTypeName = viewTypeName;
    }

    public String getImageTypeName() {
        return imageTypeName;
    }

    public void setImageTypeName(String imageTypeName) {
        this.imageTypeName = imageTypeName;
    }

    public String getCartName() {
        return cartName;
    }

    public void setCartName(String cartName) {
        this.cartName = cartName;
    }

    public List<String> getBrandNameTrans() {
        return brandNameTrans;
    }

    public void setBrandNameTrans(List<String> brandNameTrans) {
        this.brandNameTrans = brandNameTrans;
    }

    public List<String> getProductTypeTrans() {
        return productTypeTrans;
    }

    public void setProductTypeTrans(List<String> productTypeTrans) {
        this.productTypeTrans = productTypeTrans;
    }

    public List<String> getSizeTypeTrans() {
        return sizeTypeTrans;
    }

    public void setSizeTypeTrans(List<String> sizeTypeTrans) {
        this.sizeTypeTrans = sizeTypeTrans;
    }
}
