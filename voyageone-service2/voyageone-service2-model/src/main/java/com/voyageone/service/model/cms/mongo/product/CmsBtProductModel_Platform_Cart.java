package com.voyageone.service.model.cms.mongo.product;

import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
import com.voyageone.common.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 各平台的产品数据
 *
 * @author linanbin on 6/29/2016
 * @version 2.2.0
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
        return getStringAttribute("modified");
    }
    public void setModified(String modified){
        setStringAttribute("modified", modified);
    }

    //cartId
    public Integer getCartId() {
        return getIntAttribute("cartId");
    }
    public void setCartId(Integer cartId) {
        setStringAttribute("cartId", cartId);
    }

    //status
    public String getStatus() {
        return getStringAttribute("status");
    }
    public void setStatus(String status) {
        setStringAttribute("status", status);
    }

    //pCatId
    public String getpCatId() {
        return getStringAttribute("pCatId");
    }
    public void setpCatId(String pCatId) {
        setStringAttribute("pCatId", pCatId);
    }

    //pCatPath
    public String getpCatPath() {
        return getStringAttribute("pCatPath");
    }
    public void setpCatPath(String pCatPath) {
        setStringAttribute("pCatPath", pCatPath);
    }

    //pCatStatus
    public String getpCatStatus() {
        String pCatStatus = getStringAttribute("pCatStatus");
        return StringUtils.isEmpty(pCatStatus) ? "0" : pCatStatus;
    }
    public void setpCatStatus(String pCatStatus) {
        setStringAttribute("pCatStatus", StringUtils.isEmpty(pCatStatus) ? "0" : pCatStatus);
    }

    //是否为主商品
    public Integer getpIsMain() {
        return getIntAttribute("pIsMain");
    }
    public void setpIsMain(Integer pIsMain) {
        setAttribute("pIsMain", pIsMain == null ? 0 : pIsMain);
    }

    //主商品code
    public String getMainProductCode() {
        return getStringAttribute("mainProductCode");
    }
    public void setMainProductCode(String mainProductCode) {
        setStringAttribute("mainProductCode", mainProductCode);
    }

    //pProductId
    public String getpProductId() {
        return getStringAttribute("pProductId");
    }
    public void setpProductId(String pProductId) {
        setStringAttribute("pProductId", pProductId);
    }

    //pNumIId
    public String getpNumIId() {
        return getStringAttribute("pNumIId");
    }
    public void setpNumIId(String pNumIId) {
        setStringAttribute("pNumIId", pNumIId);
    }

    //pStatus
    public String getpStatus() {
        return getStringAttribute("pStatus");
    }
    public void setpStatus(String pStatus) {
        setStringAttribute("pStatus", pStatus);
    }

    //pPublishError
    public String getpPublishError() {
        return getStringAttribute("pPublishError");
    }
    public void setpPublishError(String pPublishError) {
        setStringAttribute("pPublishError", pPublishError);
    }

    //pBrandId
    public String getpBrandId() {
        return getStringAttribute("pBrandId");
    }
    public void setpBrandId(String pBrandId) {
        setStringAttribute("pBrandId", pBrandId);
    }

    //pBrandName
    public String getpBrandName() {
        return getStringAttribute("pBrandName");
    }
    public void setpBrandName(String pBrandName) {
        setStringAttribute("pBrandName", pBrandName);
    }

    //pPublishTime
    public String getpPublishTime() {
        return getStringAttribute("pPublishTime");
    }
    public void setpPublishTime(String pPublishTime) {
        setStringAttribute("pPublishTime", pPublishTime);
    }

    //pAttributeStatus
    public String getpAttributeStatus() {
        String pAttributeStatus = getStringAttribute("pAttributeStatus");
        return StringUtils.isEmpty(pAttributeStatus) ? "0" : pAttributeStatus;
    }
    public void setpAttributeStatus(String pAttributeStatus) {
        setStringAttribute("pAttributeStatus", StringUtils.isEmpty(pAttributeStatus) ? "0" : pAttributeStatus);
    }

    //pAttributeSetter
    public String getpAttributeSetter() {
        return getStringAttribute("pAttributeSetter");
    }
    public void setpAttributeSetter(String pAttributeSetter) {
        setStringAttribute("pAttributeSetter", pAttributeSetter);
    }

    //pAttributeSetTime
    public String getpAttributeSetTime() {
        return getStringAttribute("pAttributeSetTime");
    }
    public void setpAttributeSetTime(String pAttributeSetTime) {
        setStringAttribute("pAttributeSetTime", pAttributeSetTime);
    }

    //MSRP价格区间
    public Double getpPriceMsrpSt() {
        return getDoubleAttribute("pPriceMsrpSt");
    }
    public void setpPriceMsrpSt(Double pPriceMsrpSt) {
        setStringAttribute("pPriceMsrpSt", pPriceMsrpSt);
    }
    public Double getpPriceMsrpEd() {
        return getDoubleAttribute("pPriceMsrpEd");
    }
    public void setpPriceMsrpEd(Double pPriceMsrpEd) {
        setStringAttribute("pPriceMsrpEd", pPriceMsrpEd);
    }

    //建议市场价格区间
    public Double getpPriceRetailSt() {
        return getDoubleAttribute("pPriceRetailSt");
    }
    public void setpPriceRetailSt(Double priceRetailSt) {
        setStringAttribute("pPriceRetailSt", priceRetailSt);
    }
    public Double getpPriceRetailEd() {
        return getDoubleAttribute("pPriceRetailEd");
    }
    public void setpPriceRetailEd(Double priceRetailEd) {
        setStringAttribute("pPriceRetailEd", priceRetailEd);
    }

    //销售价格价格区间
    public Double getpPriceSaleSt() {
        return getDoubleAttribute("pPriceSaleSt");
    }
    public void setpPriceSaleSt(Double priceSaleSt) {
        setStringAttribute("pPriceSaleSt", priceSaleSt);
    }
    public Double getpPriceSaleEd() {
        return getDoubleAttribute("pPriceSaleEd");
    }
    public void setpPriceSaleEd(Double priceSaleEd) {
        setStringAttribute("pPriceSaleEd", priceSaleEd);
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
