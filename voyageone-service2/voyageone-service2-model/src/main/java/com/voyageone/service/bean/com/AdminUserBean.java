package com.voyageone.service.bean.com;

import com.google.common.base.Joiner;
import com.voyageone.common.configs.Channels;
import com.voyageone.common.configs.Stores;
import com.voyageone.common.util.StringUtils;
import com.voyageone.security.model.ComUserModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ethan Shi on 2016-08-15.
 */
public class AdminUserBean extends ComUserModel {

    private String storeId;

    private String channelId;

    private String roleId;

    private String roleName;

    private String application;

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

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }


    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }
    public String getChannelName()
    {
        if(StringUtils.isNullOrBlank2(channelId))
        {
            return "";
        }
        List<String> channelList = new ArrayList<String>();

        String [] channelArray = channelId.split(",");

        for(String cId : channelArray)
        {

            if("ALL".equals(cId))
            {
                channelList.add(cId);
            }
            else {
                channelList.add("("+ cId + ")" + Channels.getChannel(cId).getName());
            }
        }

        return Joiner.on(',').skipNulls().join(channelList);

    }

    public String getStoreName()
    {
        if(StringUtils.isNullOrBlank2(storeId))
        {
            return "";
        }
        List<String> storeList = new ArrayList<String>();

        String [] storeArray = storeId.split(",");

        for(String sId : storeArray)
        {
            if("ALL".equals(sId))
            {
                storeList.add(sId);
            }
            else {
                storeList.add("("+ Stores.getStore(Long.valueOf(sId)).getOrder_channel_id() + ")" + Stores.getStore(Long.valueOf(sId)).getStore_name());
            }
        }

        return Joiner.on(',').skipNulls().join(storeList);

    }
}
