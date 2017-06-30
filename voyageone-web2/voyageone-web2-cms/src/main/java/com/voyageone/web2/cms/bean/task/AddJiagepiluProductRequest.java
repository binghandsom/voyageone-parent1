package com.voyageone.web2.cms.bean.task;

import java.io.Serializable;

/**
 * 增加价格披露Task商品的请求
 *
 * @Author rex.wu
 * @Create 2017-06-22 14:52
 */
public class AddJiagepiluProductRequest implements Serializable {

    private Integer id;
    private Integer taskId;
    private String numIid;
    private String productCode;
    private Double price;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
