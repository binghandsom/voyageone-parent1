package com.voyageone.service.bean.cms;

/**
 * Created by gjl on 2016/12/21.
 */
public class CmsMtFeedConfigBean {

    private String orderChannelId;
    private String feedConfig;
    private String cfgVal1;
    private String cfgName;
    private String cfgVal2;
    private String comment;
    private String feedConfigLev;

    public String getOrderChannelId() {
        return orderChannelId;
    }

    public void setOrderChannelId(String orderChannelId) {
        this.orderChannelId = orderChannelId;
    }

    public String getCfgName() {
        return cfgName;
    }

    public void setCfgName(String cfgName) {
        this.cfgName = cfgName;
    }

    public String getCfgVal1() {
        return cfgVal1;
    }

    public void setCfgVal1(String cfgVal1) {
        this.cfgVal1 = cfgVal1;
    }

    public String getCfgVal2() {
        return cfgVal2;
    }

    public void setCfgVal2(String cfgVal2) {
        this.cfgVal2 = cfgVal2;
    }

    public String getFeedConfig() {
        return feedConfig;
    }

    public void setFeedConfig(String feedConfig) {
        this.feedConfig = feedConfig;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getFeedConfigLev() {
        return feedConfigLev;
    }

    public void setFeedConfigLev(String feedConfigLev) {
        this.feedConfigLev = feedConfigLev;
    }
}
