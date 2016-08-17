package com.voyageone.web2.admin.bean.user;
import com.voyageone.web2.admin.bean.AdminFormBean;

/**
 * Created by Ethan Shi on 2016-08-15.
 */
public class UserFormBean extends AdminFormBean {

    private String userAccount;

    private Integer active;

    private Integer orgId;

    private String channelId;

    private Integer storeId;

    private Integer roleId;

    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }

    public Integer getActive() {
        return active;
    }

    public void setActive(Integer active) {
        this.active = active;
    }

    public Integer getOrgId() {
        return orgId;
    }

    public void setOrgId(Integer orgId) {
        this.orgId = orgId;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public Integer getStoreId() {
        return storeId;
    }

    public void setStoreId(Integer storeId) {
        this.storeId = storeId;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }
}
