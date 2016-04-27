package com.voyageone.web2.core.bean;

import com.voyageone.common.configs.Enums.ChannelConfigEnums.Channel;
import com.voyageone.service.bean.com.UserConfigBean;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 会话内存储的用户信息
 * Created on 11/26/15.
 *
 * @author Jonas
 * @version 2.0.0
 */
public class UserSessionBean implements Serializable {

    /**
     * 用户Id
     */
    private int userId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 当前所选公司的所有action权限
     */
    private List<String> actionPermission;

    /**
     * 当前所选公司的所有页面权限
     */
    private List<String> pagePermission;

    /**
     * 时区
     */
    private int timeZone;

    /**
     * 用户配置
     */
    private Map<String, List<UserConfigBean>> userConfig;

    /**
     * 当前选择的 Channel (渠道)
     */
    private Channel selChannel;
    private String applicationId;
    private String application;



    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<String> getActionPermission() {
        return actionPermission;
    }

    public void setActionPermission(List<String> actionPermission) {
        this.actionPermission = actionPermission;
    }

    public List<String> getPagePermission() {
        return pagePermission;
    }

    public void setPagePermission(List<String> pagePermission) {
        this.pagePermission = pagePermission;
    }

    public int getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(int timeZone) {
        this.timeZone = timeZone;
    }

    public Map<String, List<UserConfigBean>> getUserConfig() {
        return userConfig;
    }

    public void setUserConfig(Map<String, List<UserConfigBean>> userConfig) {
        this.userConfig = userConfig;
    }

    public Channel getSelChannel() {
        return selChannel;
    }

    public void setSelChannel(Channel selChannel) {
        this.selChannel = selChannel;
    }

    public String getSelChannelId() {
        return selChannel.getId();
    }

    public void setSelChannelId(String selChannelId) {
        this.selChannel = Channel.valueOfId(selChannelId);
    }
    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }
    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }
}
