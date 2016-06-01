package com.voyageone.service.model.cms.mongo.product;

import com.voyageone.base.dao.mongodb.model.BaseMongoMap;

import java.util.List;
import java.util.Map;

/**
 * @author james.li on 2016/6/1.
 * @version 2.0.0
 */
public class CmsBtProductModel_Platform_Cart extends BaseMongoMap<String,Object>{


    public String getpCatId() {
        return getAttribute("pCatId");
    }

    public void setpCatId(String pCatId) {
        setAttribute("pCatId", pCatId);
    }

    public String getpCatPath() {
        return getAttribute("pCatPath");
    }

    public void setpCatPath(String pCatPath) {
        setAttribute("pCatPath", pCatPath);
    }

    public String getNumIid() {
        return getAttribute("numIid");
    }

    public void setNumIid(String numIid) {
        setAttribute("numIid", numIid);
    }

    public String getStatus() {
        return getAttribute("status");
    }

    public void setStatus(String status) {
        setAttribute("status", status);
    }

    public String getpStatus() {
        return getAttribute("pStatus");
    }

    public void setpStatus(String pStatus) {
        setAttribute("pStatus", pStatus);
    }

    public BaseMongoMap<String, Object> getFields() {
        return getAttribute("fields");
    }

    public void setFields(BaseMongoMap<String, Object> fields) {
        setAttribute("fields", fields);
    }

    public List<BaseMongoMap<String, Object>> getSku(){
        return getAttribute("skus");
    }
    public void setSkus(List<BaseMongoMap<String, Object>> skus){
        setAttribute("skus",skus);
    }

    public List<String> getTag(){
        return getAttribute("tag");
    }
    public void setTag(List<String> tag){
        setAttribute("tag",tag);
    }
}
