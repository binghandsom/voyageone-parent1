package com.voyageone.task2.cms.bean;

/**
 * ProductUpdateDetailBean
 * 用于返回 webservice请求的结果数据
 * Created by zero on 9/9/2015.
 *
 * @author zero
 */
public class ProductUpdateDetailBean {

    /**
     * 处理结果消息
     */
    private String resultMessage;

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
}
