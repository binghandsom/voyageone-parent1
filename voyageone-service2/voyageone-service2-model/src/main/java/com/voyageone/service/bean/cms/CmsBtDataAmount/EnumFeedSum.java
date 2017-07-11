package com.voyageone.service.bean.cms.CmsBtDataAmount;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by dell on 2016/7/5.
 */
public enum EnumFeedSum implements IEnumDataAmountSum {
    CMS_FEED_ALL("CMS_FEED_ALL", "", "/feed/product_list", "", " feed商品数"),
    CMS_FEED_STATUS_NEW("CMS_FEED_STATUS_NEW", "{'updFlg':9}", "/feed/product_list", "", "新品数"),
    CMS_FEED_STATUS_IMPORT_FINISH("CMS_FEED_STATUS_IMPORT_FINISH", "{'updFlg':1}", "/feed/product_list", "", "导入完成数"),
    CMS_FEED_STATUS_NOT_IMPORT("CMS_FEED_STATUS_NOT_IMPORT", "{'updFlg':3}", "/feed/product_list", "", "不导入数"),
    CMS_FEED_STATUS_IMPORT_FAILD("CMS_FEED_STATUS_IMPORT_FAILD", "{'updFlg':2}", "/feed/product_list", "", "导入失败数"),
    CMS_FEED_DATA_ERROR("CMS_FEED_DATA_ERROR", "{'updFlg':8}", "/feed/product_list", "", "Feed数据错误数"),
    CMS_FEED_STATUS_WAITING_FOR_IMPORT("CMS_FEED_STATUS_WAITING_FOR_IMPORT", "{'updFlg':0}", "/feed/product_list", "", "等待导入数"),
    CMS_FEED_feedBrand_block("CMS_FEED_feedBrand_block", "", "", "", "黑名单数量",1),

    //==============================================================================================
    //==================================美国CMS2枚举定义==============================================
    //==============================================================================================

    USA_CMS_FEED_STATUS_NEW("USA_CMS_FEED_STATUS_NEW", "{'status':'New'}", "/feed/usa/search", "{selfAuth:1}", "New Items"),
    USA_CMS_FEED_STATUS_PENDING("USA_CMS_FEED_STATUS_PENDING", "{'status':'New'}", "/feed/usa/search", "{selfAuth:2}", "Pending Items"),
    USA_CMS_FEED_STATUS_READY("USA_CMS_FEED_STATUS_READY", "{'status':'New'}", "/feed/usa/search", "{selfAuth:3}", "Ready Items");

    EnumFeedSum(String amountName, String strQuery, String linkUrl, String linkParameter, String comment) {
        this.amountName = amountName;
        this.strQuery = strQuery;
        this.linkUrl = linkUrl;
        this.linkParameter = linkParameter;
        this.comment = comment;
    }
    EnumFeedSum(String amountName, String strQuery, String linkUrl, String linkParameter, String comment,int sumType) {
        this.amountName = amountName;
        this.strQuery = strQuery;
        this.linkUrl = linkUrl;
        this.linkParameter = linkParameter;
        this.comment = comment;
        this.sumType=sumType;
    }
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

    private int sumType;//0:feed表统计  1：cms_bt_brand_block 黑名单表统计

    public int getDataAmountTypeId() {
        return dataAmountTypeId;
    }

    public void setDataAmountTypeId(int dataAmountTypeId) {
        this.dataAmountTypeId = dataAmountTypeId;
    }

    private  int dataAmountTypeId=EnumDataAmountType.FeedSum.getId();
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

    public static List<EnumFeedSum> getList(int sumType) {
        List<EnumFeedSum> list =new ArrayList<>();
        for (EnumFeedSum enumFeedSum : EnumFeedSum.values()) {
            if(enumFeedSum.getSumType()==sumType)
            {
                list.add(enumFeedSum);
            }
        }

        return list;
    }
}