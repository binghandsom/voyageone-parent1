package com.voyageone.task2.cms.bean;

/**
 * ProductFeedDetailBean
 * Created by zero on 7/28/2015.
 *
 * @author zero
 */
public class ProductFeedDetailBean {

    /**
     * 处理类型 1:category; 2:model; 3:product; 4:item
     */
    private int beanType;

    /**
     * 处理结果类型 0:正常插入; 1:已经存在,跳过; 2:数据为空,无需操作; -1:主键为空; -2:处理异常
     */
    private int resultType;

    /**
     * 处理结果消息
     */
    private String resultMessage;

    /**
     * 处理对象
     */
    private FeedDealObject dealObject;

    /**
     * @return the beanType
     */
    public int getBeanType() {
        return beanType;
    }

    /**
     * @param beanType the beanType to set
     */
    public void setBeanType(int beanType) {
        this.beanType = beanType;
    }

    /**
     * @return the resultType
     */
    public int getResultType() {
        return resultType;
    }

    /**
     * @param resultType the resultType to set
     */
    public void setResultType(int resultType) {
        this.resultType = resultType;
    }

    /**
     * @return the resultMessage
     */
    public String getResultMessage() {
        return resultMessage;
    }

    /**
     * @param resultMessage the resultMessage to set
     */
    public void setResultMessage(String resultMessage) {
        this.resultMessage = resultMessage;
    }

    /**
     * @return the dealObject
     */
    public FeedDealObject getDealObject() {
        return dealObject;
    }

    /**
     * @param dealObject the dealObject to set
     */
    public void setDealObject(FeedDealObject dealObject) {
        this.dealObject = dealObject;
    }
}