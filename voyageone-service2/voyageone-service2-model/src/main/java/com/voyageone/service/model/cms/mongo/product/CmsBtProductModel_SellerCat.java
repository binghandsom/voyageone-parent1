package com.voyageone.service.model.cms.mongo.product;

import com.voyageone.base.dao.mongodb.model.BaseMongoMap;

import java.util.ArrayList;
import java.util.List;

/**
 * CmsBtProductModel_SellerCat
 *
 * @author chuanyu.liang, 2016/06/03
 * @version 2.0.0
 * @since 2.0.0
 */
public class CmsBtProductModel_SellerCat extends BaseMongoMap<String, Object> {

    //cId
    public String getcId() {
        return getAttribute("cId");
    }
    public void setcId(String cId) {
        setAttribute("cId", cId);
    }

    //cName
    public String getcName() {
        return getAttribute("cName");
    }
    public void setcName(String cName) {
        setAttribute("cName", cName);
    }

    //cIds
    public List<String> getcIds() {
        return getAttribute("cIds");
    }
    public void setcIds(List<String> cIds) {
        setAttribute("cIds", cIds);
    }

    //cNames
    public List<String> getcNames() {
        return getAttribute("cNames");
    }
    public void setcNames(List<String> cNames) {
        setAttribute("cNames", cNames);
    }
}
