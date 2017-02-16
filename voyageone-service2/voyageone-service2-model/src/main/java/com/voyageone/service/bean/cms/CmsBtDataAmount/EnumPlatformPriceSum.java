package com.voyageone.service.bean.cms.CmsBtDataAmount;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

/**
 * Created by dell on 2016/7/5.
 */
public enum EnumPlatformPriceSum implements IEnumDataAmountSum{
    //Retail_Price
    CMS_PLATFORM_Retail_Price_UP("CMS_PLATFORM_Retail_Price_UP", "{'platforms.P%s.skus.priceChgFlg':{$regex:\"^U\"}}", "/search/advanceSearch", "", "指导价涨价"),
    CMS_PLATFORM_Retail_Price_DOWN("CMS_PLATFORM_Retail_Price_DOWN", "{'platforms.P%s.skus.priceChgFlg':{$regex:\"^D\"}}", "/search/advanceSearch", "", "指导价降价"),
    CMS_PLATFORM_PRICE_DOWN("CMS_PLATFORM_PRICE_DOWN", "{'platforms.P%s.cartId':'%s','platforms.P%s.skus.priceDiffFlg':'2'}", "/search/advanceSearch", "/search/advanceSearch", "比指导价低(未击穿)",(m)->{
        return String.format(m.getQueryStr(),m.getCartId(),m.getCartId(),m.getCartId());
    }),
    CMS_PLATFORM_PRICE_UP("CMS_PLATFORM_PRICE_UP", "{'platforms.P%s.cartId':'%s','platforms.P%s.skus.priceDiffFlg':'3'}", "/search/advanceSearch", "/search/advanceSearch", "比指导价高(未击穿)",(m)->{
        return String.format(m.getQueryStr(),m.getCartId(),m.getCartId(),m.getCartId());
    }),
    CMS_PLATFORM_PRICE_UP_BREAKDOWN("CMS_PLATFORM_PRICE_UP_BREAKDOWN", "{'platforms.P%s.cartId':'%s', 'platforms.P%s.skus.priceDiffFlg':'4'}", "/search/advanceSearch", "", "向上击穿警告",(m)->{
        return String.format(m.getQueryStr(),m.getCartId(),m.getCartId(),m.getCartId());
    }),
    CMS_PLATFORM_PRICE_DOWN_BREAKDOWN("CMS_PLATFORM_PRICE_DOWN_BREAKDOWN", "{'platforms.P%s.cartId':'%s','platforms.P%s.skus.priceDiffFlg':'5'}", "/search/advanceSearch", "", "向下击穿警告",(m)->{
        return String.format(m.getQueryStr(),m.getCartId(),m.getCartId(),m.getCartId());
    }),
    CMS_PLATFORM_MSRP_LESS_THAN_RETAIL_PRICE("CMS_PLATFORM_MSRP_LESS_THAN_RETAIL_PRICE", "{'platforms.P%s.skus.priceMsrpFlg':'XU'}", "/search/advanceSearch", "", "比中国建议售价高"),
    CMS_PLATFORM_priceSale_Equal_minus1("CMS_PLATFORM_priceSale_Equal_minus1", "{platforms.P%s:{$exists:true},'platforms.P%s.skus.priceRetail':-1}", "/search/advanceSearch", "", "中国最终指导价格为-1",(m)->{
        return String.format(m.getQueryStr(),m.getCartId(),m.getCartId());
    });

    EnumPlatformPriceSum(String amountName, String strQuery, String linkUrl, String linkParameter, String comment) {
        this.amountName = amountName;
        this.strQuery = strQuery;
        this.linkUrl = linkUrl;
        this.linkParameter = linkParameter;
        this.comment = comment;
    }
    EnumPlatformPriceSum(String amountName, String strQuery, String linkUrl, String linkParameter, String comment, Function<QueryStrFormatParam, String> getQuery) {
        this.amountName = amountName;
        this.strQuery = strQuery;
        this.linkUrl = linkUrl;
        this.linkParameter = linkParameter;
        this.comment = comment;
        this.funFormat=getQuery;
    }
    public Function<QueryStrFormatParam, String> getFunFormat() {
        return funFormat;
    }

    public void setFunFormat(Function<QueryStrFormatParam, String> funFormat) {
        this.funFormat = funFormat;
    }

    private Function<QueryStrFormatParam,String> funFormat;
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
    private  int dataAmountTypeId=EnumDataAmountType.PlatformPriceSum.getId();
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
    public static List<EnumPlatformPriceSum> getList() {
        List<EnumPlatformPriceSum> list = Arrays.asList(EnumPlatformPriceSum.values());
        return list;
    }
}