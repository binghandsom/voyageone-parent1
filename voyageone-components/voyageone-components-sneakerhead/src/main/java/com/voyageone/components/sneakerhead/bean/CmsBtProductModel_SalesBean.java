package com.voyageone.components.sneakerhead.bean;

import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Sales;

import java.util.Map;

/**
 * Created by gjl on 2016/11/23.
 */
public class CmsBtProductModel_SalesBean extends CmsBtProductModel_Sales {
    public final static String CODE = "code";
    //code
    public String getCode() {
        return getAttribute(CODE);
    }
    public void setCode(String code) {
        setAttribute(CODE, code);
    }
}
