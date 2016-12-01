package com.voyageone.components.sneakerhead.bean;

import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Sales;

import java.util.Map;

/**
 * CmsBtProductModel_SalesBean
 *
 * @author gjl on 2016/11/23.
 * @version 0.0.1
 */
public class CmsBtProductModel_SalesBean extends CmsBtProductModel_Sales {

    private final static String CODE = "code";

    //code
    public String getCode() {
        return getAttribute(CODE);
    }

    public void setCode(String code) {
        setAttribute(CODE, code);
    }
}
