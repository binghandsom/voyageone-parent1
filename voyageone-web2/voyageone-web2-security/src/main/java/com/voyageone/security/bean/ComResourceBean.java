package com.voyageone.security.bean;

import com.voyageone.security.model.ComResourceModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ethan Shi on 2016-08-16.
 */
public class ComResourceBean extends ComResourceModel {

    private String channelId;
    private Integer userId;

    private  int selected;

    private List<ComResourceBean> children;

    public List<ComResourceBean> getChildren() {
        return children == null ? new ArrayList<>() : children;
    }

    public void setChildren(List<ComResourceBean> children) {
        this.children = children;
    }



    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }


    public int getSelected() {
        return selected;
    }

    public void setSelected(int selected) {
        this.selected = selected;
    }

}
