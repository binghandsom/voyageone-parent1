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
}
