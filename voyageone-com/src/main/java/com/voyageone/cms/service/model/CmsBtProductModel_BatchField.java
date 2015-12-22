package com.voyageone.cms.service.model;


import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
import com.voyageone.cms.CmsConstants;
import org.omg.CORBA.INTERNAL;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 的商品Model BatchField
 * @author chuanyu.liang, 12/11/15
 * @version 2.0.0
 * @since 2.0.0
 */
public class CmsBtProductModel_BatchField extends BaseMongoMap<String, Object> {

    //code 库存
    public Integer getCodeQty() {
        return getAttribute("codeQty");
    }

    public void setCodeQty(Integer codeQty) {
        setAttribute("codeQty", codeQty);
    }


}