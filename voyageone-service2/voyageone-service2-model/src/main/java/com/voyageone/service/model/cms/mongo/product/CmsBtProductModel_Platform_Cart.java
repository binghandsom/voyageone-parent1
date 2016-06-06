package com.voyageone.service.model.cms.mongo.product;

import com.voyageone.base.dao.mongodb.model.BaseMongoMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author james.li on 2016/6/1.
 * @version 2.0.0
 */
public class CmsBtProductModel_Platform_Cart extends BaseMongoMap<String,Object>{

    public final static String FIELDS = "fields";
    public final static String SKUS = "skus";

    public CmsBtProductModel_Platform_Cart(){

    }

    public CmsBtProductModel_Platform_Cart(Map<String,Object> map){
        this.putAll(map);
    }
    //cartId
    public String getCatrId() {
        return getAttribute("cartId");
    }
    public void setCartId(String cartId) {
        setAttribute("cartId", cartId);
    }

    //pCatId
    public String getpCatId() {
        return getAttribute("pCatId");
    }
    public void setpCatId(String pCatId) {
        setAttribute("pCatId", pCatId);
    }

    //pCatPath
    public String getpCatPath() {
        return getAttribute("pCatPath");
    }
    public void setpCatPath(String pCatPath) {
        setAttribute("pCatPath", pCatPath);
    }

    //pCatStatus
    public String getpCatStatus() {
        return getAttribute("pCatStatus");
    }
    public void setpCatStatus(String pCatStatus) {
        setAttribute("pCatStatus", pCatStatus);
    }

    // 是否为主商品
    public int getpIsMain() {
        return getAttribute("pIsMain");
    }
    public void setpIsMain(int pIsMain) {
        setAttribute("pIsMain", pIsMain);
    }

    //pNumIid
    public String getpNumIid() {
        return getAttribute("pNumIid");
    }
    public void setpNumIid(String pNumIid) {
        setAttribute("pNumIid", pNumIid);
    }

    //status
    public String getStatus() {
        return getAttribute("status");
    }
    public void setStatus(String status) {
        setAttribute("status", status);
    }

    //pStatus
    public String getpStatus() {
        return getAttribute("pStatus");
    }
    public void setpStatus(String pStatus) {
        setAttribute("pStatus", pStatus);
    }

    //pPubishError
    public String getpPubishError() {
        return getAttribute("pPubishError");
    }
    public void setpPubishError(String pPubishError) {
        setAttribute("pPubishError", pPubishError);
    }

    //pBrandId
    public String getpBrandId() {
        return getAttribute("pBrandId");
    }
    public void setpBrandIds(String pBrandId) {
        setAttribute("pBrandId", pBrandId);
    }

    //pBrandName
    public String getpBrandName() {
        return getAttribute("pBrandName");
    }
    public void setpBrandName(String pBrandName) {
        setAttribute("pBrandName", pBrandName);
    }

    //pPublishTime
    public String getpPublishTime() {
        return getAttribute("pPublishTime");
    }
    public void setpPublishTime(String pPublishTime) {
        setAttribute("pPublishTime", pPublishTime);
    }

    //pAttributeStatus
    public String getpAttributeStatus() {
        return getAttribute("pAttributeStatus");
    }
    public void setpAttributeStatus(String pAttributeStatus) {
        setAttribute("pAttributeStatus", pAttributeStatus);
    }

    //pAttributeSetter
    public String getpAttributeSetter() {
        return getAttribute("pAttributeSetter");
    }
    public void setpAttributeSetter(String pAttributeSetter) {
        setAttribute("pAttributeSetter", pAttributeSetter);
    }

    //pAttributeSetTime
    public String getpAttributeSetTime() {
        return getAttribute("pAttributeSetTime");
    }
    public void setpAttributeSetTime(String pAttributeSetTime) {
        setAttribute("pAttributeSetTime", pAttributeSetTime);
    }

    //fields
    public BaseMongoMap<String, Object> getFields() {
        return getAttribute("fields");
    }
    public void setFields(BaseMongoMap<String, Object> fields) {
        setAttribute("fields", fields);
    }

    //skus
    public List<BaseMongoMap<String, Object>> getSkus(){
        return getAttribute("skus");
    }
    public void setSkus(List<BaseMongoMap<String, Object>> skus){
        setAttribute("skus",skus);
    }

    //sellerCats
    public List<CmsBtProductModel_SellerCat> getSellerCats(){
        return getAttribute("sellerCats");
    }
    public void setSellerCats(List<CmsBtProductModel_SellerCat> sellerCats){
        setAttribute("sellerCats",sellerCats);
    }

    //tag
    public List<String> getTag(){
        return getAttribute("tag");
    }
    public void setTag(List<String> tag){
        setAttribute("tag",tag);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object put(String key, Object value) {
        // fields
        if (FIELDS.equals(key)) {
            if (value != null) {
                Map<String, Object> map = (Map<String, Object>) value;
                BaseMongoMap<String, Object> fields;
                if (map instanceof BaseMongoMap) {
                    fields = (BaseMongoMap<String, Object>) map;
                } else {
                    fields = new BaseMongoMap<>();
                    fields.putAll(map);
                }
                value = fields;
            }
        }

        // skus
        if (SKUS.equals(key)) {
            if (value != null) {
                List<Map<String, Object>> imageMaps = (List<Map<String, Object>>) value;
                List<BaseMongoMap<String, Object>> skus = new ArrayList<>();
                for (Map<String, Object> map : imageMaps) {
                    if (map != null) {
                        BaseMongoMap<String, Object> sku;
                        if (map instanceof BaseMongoMap) {
                            sku = (BaseMongoMap<String, Object>) map;
                        } else {
                            sku = new BaseMongoMap<>();
                            sku.putAll(map);
                        }
                        skus.add(sku);
                    }
                }
                value = skus;
            }
        }
        return super.put(key, value);
    }
}
