package com.voyageone.service.bean.cms.CmsBtDataAmount;

import java.util.Arrays;
import java.util.List;

/**
 * Created by dell on 2016/7/5.
 */
public enum EnumMasterSum implements IEnumDataAmountSum{
    CMS_MASTER_NO_CATEGORY("CMS_MASTER_NO_CATEGORY", "{'common.fields.isMasterMain':1,'common.fields.categoryStatus':{$in:[null,0]}}", "", "", "等待设置主类目数"),
    CMS_MASTER_NO_HSCODE("CMS_MASTER_NO_HSCODE", "{'common.fields.isMasterMain':1,'common.fields.hsCodeStatus':{$in:[null,0]}}", "", "", "等待设置税号数"),
    CMS_MASTER_UNTRANSLATED("CMS_MASTER_UNTRANSLATED", "{'common.fields.isMasterMain':1,'common.fields.translateStatus':{$in:[null,0]}}", "", "", "等待翻译数");
    EnumMasterSum(String amountName, String strQuery, String linkUrl, String linkParameter, String comment) {
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

    private  int dataAmountTypeId=2;
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

    public static List<EnumMasterSum> getList() {
        List<EnumMasterSum> list = Arrays.asList(EnumMasterSum.values());
        return list;
    }
}