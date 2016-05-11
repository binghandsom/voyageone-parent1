package com.voyageone.service.bean.cms;

import com.voyageone.service.model.cms.mongo.channel.CmsBtImageGroupModel;
import com.voyageone.service.model.cms.mongo.channel.CmsBtImageTemplateModel;

import java.util.List;

/**
 * Created by gjl on 2016/4/27.
 */
public class CmsBtImageTemplateBean extends CmsBtImageTemplateModel {
    private String viewTypeName;
    private String cartName;
    private String imageTemplateTypeName;

    public String getImageTemplateTypeName() {
        return imageTemplateTypeName;
    }

    public void setImageTemplateTypeName(String imageTemplateTypeName) {
        this.imageTemplateTypeName = imageTemplateTypeName;
    }

    private List<String> brandNameTrans;
    private List<String> productTypeTrans;
    private List<String> sizeTypeTrans;

    public String getViewTypeName() {
        return viewTypeName;
    }
    public void setViewTypeName(String viewTypeName) {
        this.viewTypeName = viewTypeName;
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
