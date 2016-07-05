package com.voyageone.service.bean.cms.CmsBtDataAmount;

import java.util.Arrays;
import java.util.List;

/**
 * Created by dell on 2016/7/5.
 */
public enum EnumPlatformInfoSum implements IEnumDataAmountSum{
    CMS_PLATFORM_ALL("CMS_PLATFORM_ALL", "{platform.P%s:{$exists:true}}", "", "", "商品数"),
    CMS_PLATFORM_NO_CATEGORY("CMS_PLATFORM_NO_CATEGORY", "{'platform.P%s.pCatStatus':{$in:[null,0]}}", "", "", "等待设置平台类目商品数"),
    CMS_PLATFORM_NO_ATTRIBUTE("CMS_PLATFORM_NO_ATTRIBUTE", "{'platform.P%s.pAttributeStatus':{$in:[null,0]}}", "", "", "等待设置属性数"),
    CMS_PLATFORM_READY("CMS_PLATFORM_READY", "{'platform.P%s.status':'Ready'}", "", "", "等待Approved数"),
    CMS_PLATFORM_WAITING_PUBLISH("CMS_PLATFORM_WAITING_PUBLISH", "{'platform.P%s.pStatus':'WaitingPublish'}", "", "", "等待上新数"),
    CMS_PLATFORM_PUBLISH_SUCCESS("CMS_PLATFORM_PUBLISH_SUCCESS", "{'platform.P%s.pPublishError':{$in:[null,0]}}", "", "", "上新成功数"),
    CMS_PLATFORM_PUBLISH_FAILD("CMS_PLATFORM_PUBLISH_FAILD", "{'platform.P%s.pPublishError':{$nin:[null,0]}}", "", "", "上新失败数");
    EnumPlatformInfoSum(String amountName, String strQuery, String linkUrl, String linkParameter, String comment) {
        this.amountName = amountName;
        this.strQuery = strQuery;
        this.linkUrl = linkUrl;
        this.linkParameter = linkParameter;
        this.comment = comment;
    }

    private String amountName;//
    private String strQuery;//查询条件
    private String linkUrl;//链接地址
    private String linkParameter;// 链接参数
    private String comment;//备注

    public int getDataAmountTypeId() {
        return dataAmountTypeId;
    }

    public void setDataAmountTypeId(int dataAmountTypeId) {
        this.dataAmountTypeId = dataAmountTypeId;
    }

    private  int dataAmountTypeId=4;
    public String getAmountName() {
        return amountName;
    }
    public void setAmountName(String amountName) {
        this.amountName = amountName;
    }
    public String getStrQuery() {
        return strQuery;
    }
    public void setStrQuery(String strQuery) {
        this.strQuery = strQuery;
    }
    public String getLinkUrl() {
        return linkUrl;
    }
    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }
    public String getLinkParameter() {
        return linkParameter;
    }
    public void setLinkParameter(String linkParameter) {
        this.linkParameter = linkParameter;
    }
    public String getComment() {
        return comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }

    public static List<EnumPlatformInfoSum> getList() {
        List<EnumPlatformInfoSum> list = Arrays.asList(EnumPlatformInfoSum.values());
        return list;
    }
}