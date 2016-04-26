package com.voyageone.service.model.cms.mongo.product;


import com.voyageone.base.dao.mongodb.model.BaseMongoMap;

/**
 * 的商品Model BatchField
 * @author chuanyu.liang, 12/11/15
 * @version 2.0.0
 * @since 2.0.0
 */
public class CmsBtProductModel_BatchField extends BaseMongoMap<String, Object> {

    //code 库存
    public Integer getCodeQty() {
        Object qty = getAttribute("codeQty");
        if (qty == null) {
            return 0;
        } else {
            return (Integer) qty;
        }
    }

    public void setCodeQty(Integer codeQty) {
        setAttribute("codeQty", codeQty);
    }


}