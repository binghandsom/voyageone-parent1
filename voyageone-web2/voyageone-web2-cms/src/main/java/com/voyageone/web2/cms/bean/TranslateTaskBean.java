package com.voyageone.web2.cms.bean;

import java.util.List;

/**
 * Created by lewis on 2016/2/19.
 */
public class TranslateTaskBean {

    /**
     * 用户已完成的数量.
     */
    private int userDoneCount;

    /**
     * 已完成的数量.
     */
    private int totalDoneCount;

    /**
     * 所有未完成的数量.
     */
    private int totalUndoneCount;

    /**
     * 任务分配策略,1:只分配主商品，0:或者所有的商品都可以分配.
     */
    private int distributeRule;

    /**
     * 获取任务数量.
     */
    private int distributeCount;

    /**
     * 更新时间.
     */
    private String modifiedTime;

    /**
     * 模糊查询条件.
     */
    private String searchCondition;

    /**
     * 排序条件.
     */
    private String sortCondition;

    /**
     * 排序规则.
     */
    private String sortRule;

    /**
     * 待翻译商品信息列表.
     */
    List<ProductTranslationBean> productTranslationBeanList;

    public long getProdListTotal() {
        return prodListTotal;
    }

    public void setProdListTotal(long prodListTotal) {
        this.prodListTotal = prodListTotal;
    }

    /**
     * 待翻译商品信息列表总数.
     */
    private long prodListTotal;

    public int getUserDoneCount() {
        return userDoneCount;
    }

    public void setUserDoneCount(int userDoneCount) {
        this.userDoneCount = userDoneCount;
    }

    public int getTotalDoneCount() {
        return totalDoneCount;
    }

    public void setTotalDoneCount(int totalDoneCount) {
        this.totalDoneCount = totalDoneCount;
    }

    public int getTotalUndoneCount() {
        return totalUndoneCount;
    }

    public void setTotalUndoneCount(int totalUndoneCount) {
        this.totalUndoneCount = totalUndoneCount;
    }

    public int getDistributeCount() {
        return distributeCount;
    }

    public void setDistributeCount(int distributeCount) {
        this.distributeCount = distributeCount;
    }

    public int getDistributeRule() {
        return distributeRule;
    }

    public void setDistributeRule(int distributeRule) {
        this.distributeRule = distributeRule;
    }

    public List<ProductTranslationBean> getProductTranslationBeanList() {
        return productTranslationBeanList;
    }

    public void setProductTranslationBeanList(List<ProductTranslationBean> productTranslationBeanList) {
        this.productTranslationBeanList = productTranslationBeanList;
    }

    public String getModifiedTime() {
        return modifiedTime;
    }

    public void setModifiedTime(String modifiedTime) {
        this.modifiedTime = modifiedTime;
    }

    public String getSearchCondition() {
        return searchCondition;
    }

    public void setSearchCondition(String searchCondition) {
        this.searchCondition = searchCondition;
    }

    public String getSortCondition() {
        return sortCondition;
    }

    public void setSortCondition(String sortCondition) {
        this.sortCondition = sortCondition;
    }

    public String getSortRule() {
        return sortRule;
    }

    public void setSortRule(String sortRule) {
        this.sortRule = sortRule;
    }

}
