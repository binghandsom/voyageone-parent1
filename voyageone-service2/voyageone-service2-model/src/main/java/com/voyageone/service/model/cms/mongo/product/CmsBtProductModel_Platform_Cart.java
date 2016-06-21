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
    public final static String SELLER_CATS = "sellerCats";

    public CmsBtProductModel_Platform_Cart(){

    }

    public CmsBtProductModel_Platform_Cart(Map<String,Object> map){
        this.putAll(map);
    }

    //modified
    public String getModified(){
        return getAttribute("modified");
    }
    public void setModified(String modified){
        setAttribute("modified", modified);
    }

    //cartId
    public int getCartId() {
        return getIntAttribute("cartId");
    }
    public void setCartId(int cartId) {
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

    //pProductId
    public String getpProductId() {
        return getAttribute("pProductId");
    }
    public void setpProductId(String pNumIId) {
        setAttribute("pProductId", pNumIId);
    }

    //pNumIId
    public String getpNumIId() {
        return getAttribute("pNumIId");
    }
    public void setpNumIId(String pNumIId) {
        setAttribute("pNumIId", pNumIId);
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

    //pPublishError
    public String getpPublishError() {
        return getAttribute("pPublishError");
    }
    public void setpPublishError(String pPublishError) {
        setAttribute("pPublishError", pPublishError);
    }

    //pBrandId
    public String getpBrandId() {
        return getAttribute("pBrandId");
    }
    public void setpBrandId(String pBrandId) {
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

    //MSRP价格区间
    public Double getpPriceMsrpSt() {
        return getDoubleAttribute("pPriceMsrpSt");
    }
    public void setpPriceMsrpSt(Double pPriceMsrpSt) {
        setAttribute("pPriceMsrpSt", pPriceMsrpSt);
    }
    public Double getpPriceMsrpEd() {
        return getDoubleAttribute("pPriceMsrpEd");
    }
    public void setpPriceMsrpEd(Double pPriceMsrpEd) {
        setAttribute("pPriceMsrpEd", pPriceMsrpEd);
    }

    //建议市场价格区间
    public Double getpPriceRetailSt() {
        return getDoubleAttribute("pPriceRetailSt");
    }
    public void setpPriceRetailSt(Double priceRetailSt) {
        setAttribute("pPriceRetailSt", priceRetailSt);
    }
    public Double getpPriceRetailEd() {
        return getDoubleAttribute("pPriceRetailEd");
    }
    public void setpPriceRetailEd(Double priceRetailEd) {
        setAttribute("pPriceRetailEd", priceRetailEd);
    }

    //销售价格价格区间
    public Double getpPriceSaleSt() {
        return getDoubleAttribute("pPriceSaleSt");
    }
    public void setpPriceSaleSt(Double priceSaleSt) {
        setAttribute("pPriceSaleSt", priceSaleSt);
    }
    public Double getpPriceSaleEd() {
        return getDoubleAttribute("pPriceSaleEd");
    }
    public void setpPriceSaleEd(Double priceSaleEd) {
        setAttribute("pPriceSaleEd", priceSaleEd);
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

        //sellerCats
        if (SELLER_CATS.equals(key)) {
            if (value != null) {
                List<Map<String, Object>> maps = (List<Map<String, Object>>) value;
                List<CmsBtProductModel_SellerCat> sellerCatMaps = new ArrayList<>();
                for (Map<String, Object> map : maps) {
                    if (map != null) {
                        CmsBtProductModel_SellerCat sellerCat;
                        if (map instanceof CmsBtProductModel_SellerCat) {
                            sellerCat = (CmsBtProductModel_SellerCat) map;
                        } else {
                            sellerCat = new CmsBtProductModel_SellerCat();
                            sellerCat.putAll(map);
                        }
                        sellerCatMaps.add(sellerCat);
                    }
                }
                value = sellerCatMaps;
            }
        }

        return super.put(key, value);
    }
}
