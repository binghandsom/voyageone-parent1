package com.voyageone.service.bean.cms.CmsBtDataAmount;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * Created by dell on 2016/7/5.
 */
public enum EnumPlatformInfoSum implements IEnumDataAmountSum {
    CMS_PLATFORM_ALL("CMS_PLATFORM_ALL", "{platforms.P%s:{$exists:true},platforms.P%s:{$exists:true}}", "/search/advanceSearch", "", "商品数", (m) -> {
        return String.format(m.getQueryStr(), m.getCartId(), m.getCartId());
    }),//{'platforms.P#.cartId':#},{'platforms.P#.pCatStatus':{$in:[null,'','0']}
    CMS_PLATFORM_NO_CATEGORY("CMS_PLATFORM_NO_CATEGORY", "{platforms.P%s:{$exists:true},'platforms.P%s.pCatStatus':{$in:[null,'','0']}}", "/search/advanceSearch", "", "等待设置平台类目商品数", (m) -> {
        return String.format(m.getQueryStr(), m.getCartId(), m.getCartId());
    }),//[{'platforms.P#.cartId':#},{'platforms.P#.pAttributeStatus':{$in:[null,'','0']}
    CMS_PLATFORM_NO_ATTRIBUTE("CMS_PLATFORM_NO_ATTRIBUTE", "{platforms.P%s:{$exists:true},'platforms.P%s.pAttributeStatus':{$in:[null,'','0']}}", "/search/advanceSearch", "", "等待设置属性数", (m) -> {
        return String.format(m.getQueryStr(), m.getCartId(), m.getCartId());
    }),
    CMS_PLATFORM_READY("CMS_PLATFORM_READY", "{'platforms.P%s.status':'Ready'}", "", "", "等待Approved数"),
    // query:={$and:[{'platforms.P#.cartId':#},{'platforms.P#.pStatus':{$in:#}},{'platforms.P#.status':{$in:#}}]}
    CMS_PLATFORM_WAITING_PUBLISH("CMS_PLATFORM_WAITING_PUBLISH", "{'platforms.P%s.pStatus':'WaitingPublish','platforms.P%s.status':'Approved'}", "/search/advanceSearch", "", "等待上新数", (m) -> {
        return String.format(m.getQueryStr(), m.getCartId(), m.getCartId());
    }),
    CMS_PLATFORM_PUBLISH_SUCCESS("CMS_PLATFORM_PUBLISH_SUCCESS", "{'platforms.P%s.pPublishError':{$in:[null,'']},'platforms.P%s.status':'Approved','platforms.P%s.pStatus':{$in:['InStock','OnSale']}}", "/search/advanceSearch", "", "上新成功数", (m) -> {
        return String.format(m.getQueryStr(), m.getCartId(), m.getCartId(), m.getCartId());
    }),//
    CMS_PLATFORM_PUBLISH_FAILD("CMS_PLATFORM_PUBLISH_FAILD", "{'platforms.P%s.pPublishError':{$nin:[null,'']}}", "/search/advanceSearch", "", "上新失败数"),
    //{platforms.P%s:{$exists:true},$where:'this.platforms.P%s.pStatus!=this.platforms.P%s.pReallyStatus'}
    CMS_PLATFORM_pStatus_pReallyStatus_notEqual("CMS_PLATFORM_PSTATUS_REALLYSTATUS_NOT_EQUAL", "{$or:[{'platforms.P%s.pReallyStatus':'OnSale','platforms.P%s.pStatus':{$ne:'OnSale'}},{'platforms.P%s.pReallyStatus':'InStock','platforms.P%s.pStatus':{$ne:'InStock'}}]}", "/search/advanceSearch", "", "商品平台状态与实际相异数", (m) -> {
        return String.format(m.getQueryStr(), m.getCartId(), m.getCartId(), m.getCartId(), m.getCartId());
    }),// priceSale
    CMS_PLATFORM_BRAND_BLOCK("CMS_PLATFORM_BRAND_BLOCK", "", "", "", "黑名单数量", 1),

    CMS_PLATFORM_NEW_SKU("CMS_PLATFORM_NEW_SKU", "{'platforms.P%s.status':'Approved','platforms.P%s.isNewSku':'1'}", "/search/advanceSearch", "", "上新商品中有未上新SKU商品数", (m) -> {
        return String.format(m.getQueryStr(), m.getCartId(), m.getCartId());
    }),
    CMS_PLATFORM_pStatus_OnSale_pReallyStatus_InStock("CMS_PLATFORM_PSTATUS_ONSALE_PREALLYSTATUS_INSTOCK","{lock:'0',\"common.fields.quantity\":{ $gt:0},\"platforms.P%s.pStatus\":'OnSale',\"platforms.P%s.pReallyStatus\":'InStock'}","", "", "下架有库存商品数", (m) -> {
        return String.format(m.getQueryStr(), m.getCartId(), m.getCartId());
    }),
    CMS_PLATFORM_pStatus_OnSale_pReallyStatus_Violation("CMS_PLATFORM_PSTATUS_ONSALE_PREALLYSTATUS_VIOLATION","{lock:'0',\"platforms.P%s.pReallyStatus\":'Violation'}","", "", "违规下架商品数", (m) -> {
        return String.format(m.getQueryStr(), m.getCartId(), m.getCartId());
    }),

    // =============================================================================================
    // =====================================美国CMS2 常量定义=========================================
    //==============================================================================================
    USA_CMS_PLATFORMS_AMOUNT("%s Items", "", "", "", "Total product int Platform(%s)");


    EnumPlatformInfoSum(String amountName, String strQuery, String linkUrl, String linkParameter, String comment) {
        this.amountName = amountName;
        this.strQuery = strQuery;
        this.linkUrl = linkUrl;
        this.linkParameter = linkParameter;
        this.comment = comment;
    }

    EnumPlatformInfoSum(String amountName, String strQuery, String linkUrl, String linkParameter, String comment, int sumType) {
        this.amountName = amountName;
        this.strQuery = strQuery;
        this.linkUrl = linkUrl;
        this.linkParameter = linkParameter;
        this.comment = comment;
        this.sumType = sumType;
    }

    EnumPlatformInfoSum(String amountName, String strQuery, String linkUrl, String linkParameter, String comment, Function<QueryStrFormatParam, String> getQuery) {
        this.amountName = amountName;
        this.strQuery = strQuery;
        this.linkUrl = linkUrl;
        this.linkParameter = linkParameter;
        this.comment = comment;
        this.funFormat = getQuery;
    }


    private int sumType;//0:CmsBtProduct表统计  1：cms_bt_brand_block 黑名单表统计
    private String amountName;//
    private String strQuery;//查询条件
    private String linkUrl;//链接地址
    private String linkParameter;// 链接参数
    private String comment;//备注

    public Function<QueryStrFormatParam, String> getFunFormat() {
        return funFormat;
    }

    public void setFunFormat(Function<QueryStrFormatParam, String> funFormat) {
        this.funFormat = funFormat;
    }

    private Function<QueryStrFormatParam, String> funFormat;

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

    private int dataAmountTypeId = EnumDataAmountType.PlatformInfoSum.getId();

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

    public static List<EnumPlatformInfoSum> getList(int sumType) {
        List<EnumPlatformInfoSum> list = new ArrayList<>();
        for (EnumPlatformInfoSum enumPlatformInfoSum : EnumPlatformInfoSum.values()) {
            if (enumPlatformInfoSum.getSumType() == sumType) {
                list.add(enumPlatformInfoSum);
            }
        }
        return list;
    }
}