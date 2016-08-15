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

    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }

    @Override
    public Integer getActive() {
        return active;
    }

    @Override
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


}
