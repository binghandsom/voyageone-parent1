package com.voyageone.service.bean.cms.CmsBtDataAmount;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

/**
 * Created by dell on 2016/7/5.
 */
public enum EnumPlatformInfoSum_BrandNoMatch implements IEnumDataAmountSum{
    CMS_BRAND_NO_MATCH_COUNT("CMS_BRAND_NO_MATCH_COUNT", "", "", "", "品牌未匹配数量");
    EnumPlatformInfoSum_BrandNoMatch(String amountName, String strQuery, String linkUrl, String linkParameter, String comment) {
        this.amountName = amountName;
        this.strQuery = strQuery;
        this.linkUrl = linkUrl;
        this.linkParameter = linkParameter;
        this.comment = comment;
    }
    EnumPlatformInfoSum_BrandNoMatch(String amountName, String strQuery, String linkUrl, String linkParameter, String comment, Function<QueryStrFormatParam, String> getQuery) {
        this.amountName = amountName;
        this.strQuery = strQuery;
        this.linkUrl = linkUrl;
        this.linkParameter = linkParameter;
        this.comment = comment;
        this.funFormat=getQuery;
    }
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

    private Function<QueryStrFormatParam,String> funFormat;


    public int getDataAmountTypeId() {
        return dataAmountTypeId;
    }

    public void setDataAmountTypeId(int dataAmountTypeId) {
        this.dataAmountTypeId = dataAmountTypeId;
    }

    private  int dataAmountTypeId=EnumDataAmountType.PlatformInfoSum.getId();
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

    public static List<EnumPlatformInfoSum_BrandNoMatch> getList() {
        List<EnumPlatformInfoSum_BrandNoMatch> list = Arrays.asList(EnumPlatformInfoSum_BrandNoMatch.values());
        return list;
    }
}