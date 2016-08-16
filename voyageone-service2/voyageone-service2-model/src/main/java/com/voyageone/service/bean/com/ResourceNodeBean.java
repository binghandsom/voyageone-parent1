package com.voyageone.service.bean.com;

import com.voyageone.security.model.ComResourceModel;

import java.util.List;

/**
 * Created by Ethan Shi on 2016-08-16.
 */
public class ResourceNodeBean extends ComResourceModel {

    List<ResourceNodeBean> childewn;


    public List<ResourceNodeBean> getChildewn() {
        return childewn;
    }

    public void setChildewn(List<ResourceNodeBean> childewn) {
        this.childewn = childewn;
    }
}
