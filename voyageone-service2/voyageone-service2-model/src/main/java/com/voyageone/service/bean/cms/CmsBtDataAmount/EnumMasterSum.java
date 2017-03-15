package com.voyageone.service.bean.cms.CmsBtDataAmount;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dell on 2016/7/5.
 */
public enum EnumMasterSum implements IEnumDataAmountSum{
    CMS_MASTER_NO_CATEGORY("CMS_MASTER_NO_CATEGORY", "{'lock':'0','common.fields.isMasterMain':1,'common.fields.categoryStatus':{$in:[null,'','0']}}", "", "", "等待设置主类目数"),
    CMS_MASTER_NO_HSCODE("CMS_MASTER_NO_HSCODE", "{'lock':'0','common.fields.hsCodeStatus':{$in:[null,'','0']}}", "", "", "等待设置税号数"),
    CMS_MASTER_UNTRANSLATED("CMS_MASTER_UNTRANSLATED", "{'lock':'0','common.fields.isMasterMain':1,'common.fields.translateStatus':{$in:[null,'','0']}}", "", "", "等待翻译数"),
    CMS_MASTER_Brand_block("CMS_MASTER_Brand_block", "", "", "", "黑名单数量",1),
    CMS_MASTER_NO_SALE("CMS_MASTER_NO_SALE", "{'common.fields.quantity':{$gt:0}, 'platforms.P20.pNumIId':{$in:['',null]}, 'platforms.P21.pNumIId':{$in:['',null]}, 'platforms.P22.pNumIId':{$in:['',null]}, 'platforms.P23.pNumIId':{$in:['',null]}, 'platforms.P24.pNumIId':{$in:['',null]}, 'platforms.P25.pNumIId':{$in:['',null]}, 'platforms.P26.pNumIId':{$in:['',null]}, 'platforms.P27.pNumIId':{$in:['',null]}}", "", "", "有库存没上线");

    EnumMasterSum(String amountName, String strQuery, String linkUrl, String linkParameter, String comment) {
        this.amountName = amountName;
        this.strQuery = strQuery;
        this.linkUrl = linkUrl;
        this.linkParameter = linkParameter;
        this.comment = comment;
    }
    EnumMasterSum(String amountName, String strQuery, String linkUrl, String linkParameter, String comment,int sumType) {
        this.amountName = amountName;
        this.strQuery = strQuery;
        this.linkUrl = linkUrl;
        this.linkParameter = linkParameter;
        this.comment = comment;
        this.sumType=sumType;
    }
    private int sumType;//0:CmsBtProduct表统计  1：cms_bt_brand_block 黑名单表统计
    private String amountName;//
    private String strQuery;//查询条件
    private String linkUrl;//链接地址
    private String linkParameter;// 链接参数
    private String comment;//备注
    public int getSumType() {
        return sumType;
    }

    public void setSumType(int sumType) {
        this.sumType = sumType;
    }

    public int getDataAmountTypeId() {
        return dataAmountTypeId;
    }

    public void setDataAmountTypeId(int dataAmountTypeId) {
        this.dataAmountTypeId = dataAmountTypeId;
    }

    private  int dataAmountTypeId=EnumDataAmountType.MasterSum.getId();
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

    public static List<EnumMasterSum> getList(int sumType) {
        List<EnumMasterSum> list =new ArrayList<>();
        for (EnumMasterSum enumSum : EnumMasterSum.values()) {
            if(enumSum.getSumType()==sumType)
            {
                list.add(enumSum);
            }
        }
        return list;
    }
}