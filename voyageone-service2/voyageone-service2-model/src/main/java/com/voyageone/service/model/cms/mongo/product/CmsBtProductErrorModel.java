package com.voyageone.service.model.cms.mongo.product;

import com.voyageone.base.dao.mongodb.model.BaseMongoModel;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link CmsBtProductErrorModel} 的商品Model
 *
 * @author linanbin on 6/29/2016
 * @author chuanyu.liang, 12/11/15
 * @version 2.0.0
 * @since 2.2.0
 */
public class CmsBtProductErrorModel  extends BaseMongoModel {

    // 原始channelId
    private String channelId;

    private String product_Id;

    private String errorInfo = "";

    private List<String> errors = new ArrayList<>();

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getProduct_Id() {
        return product_Id;
    }

    public void setProduct_Id(String product_Id) {
        this.product_Id = product_Id;
    }

    public String getErrorInfo() {
        return errorInfo;
    }

    public void setErrorInfo(String errorInfo) {
        this.errorInfo = errorInfo;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }
}