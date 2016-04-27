package com.voyageone.service.bean.com;

import java.util.List;
import java.util.Map;

/**
 * 渠道下应用系统的权限
 * Created on 11/30/15.
 *
 * @author Jonas
 * @version 2.0.0
 */
public class ChannelPermissionBean {

    private int companyId;

    private String companyName;

    private String channelId;

    private String channelName;

    private String channelImgUrl;

    private List<Map<String,Object>> apps;

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getChannelImgUrl() {
        return channelImgUrl;
    }

    public void setChannelImgUrl(String channelImgUrl) {
        this.channelImgUrl = channelImgUrl;
    }

    public  List<Map<String,Object>> getApps() {
        return apps;
    }

    public void setApps(List<Map<String,Object>> apps) {
        this.apps = apps;
    }
}
