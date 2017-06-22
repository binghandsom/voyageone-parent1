package com.voyageone.web2.cms.bean.task;

import java.io.Serializable;

/**
 * 增加价格披露Task商品的请求
 *
 * @Author rex.wu
 * @Create 2017-06-22 14:52
 */
public class AddJiagepiluProductRequest implements Serializable {

    private Integer taskId;
    private String numIid;
    private String code;
    private Double price;

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public String getNumIid() {
        return numIid;
    }

    public void setNumIid(String numIid) {
        this.numIid = numIid;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
