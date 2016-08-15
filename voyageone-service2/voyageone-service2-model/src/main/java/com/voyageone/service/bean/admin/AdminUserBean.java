package com.voyageone.service.bean.admin;

import com.voyageone.security.model.ComUserModel;

/**
 * Created by Ethan Shi on 2016-08-15.
 */
public class AdminUserBean extends ComUserModel {

    private String storeId;

    private String channelId;

    private String roleIds;

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(String roleIds) {
        this.roleIds = roleIds;
    }

}
