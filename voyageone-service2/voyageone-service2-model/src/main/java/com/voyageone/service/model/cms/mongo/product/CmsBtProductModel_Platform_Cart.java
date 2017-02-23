package com.voyageone.service.model.cms.mongo.product;

import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.bean.cms.product.ProductMqqBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 各平台的产品数据
 *
 * @author linanbin on 6/29/2016
 * @author james.li on 2016/6/1.
 * @version 2.0.0
 */
public class CmsBtProductModel_Platform_Cart extends CmsBtProductModel_Images {

    public final static String FIELDS = "fields";
    public final static String SKUS = "skus";
    public final static String SELLER_CATS = "sellerCats";
    public final static String MQQ = "mqq";

    public CmsBtProductModel_Platform_Cart() {

    }

    public CmsBtProductModel_Platform_Cart(Map<String, Object> map) {
        this.putAll(map);
    }

    //modified
    public String getModified() {
        return getStringAttribute("modified");
    }

    public void setModified(String modified) {
        setStringAttribute("modified", modified);
    }

    //cartId
    public Integer getCartId() {
        return getIntAttribute("cartId");
    }

    public void setCartId(Integer cartId) {
        setAttribute("cartId", cartId);
    }

    //status
    public String getStatus() {
        return getStringAttribute("status");
    }

    public void setStatus(String status) {
        setStringAttribute("status", status);
    }

    public void setStatus(CmsConstants.ProductStatus status) {
        String value = null;
        if (status != null) {
            value = status.name();
        }
        setAttribute("status", value);
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

    //pPlatformMallId
    public String getpPlatformMallId() {
        return getStringAttribute("pPlatformMallId");
    }

    public void setpPlatformMallId(String pPlatformMallId) {
        setStringAttribute("pPlatformMallId", pPlatformMallId);
    }

    //pStatus
    public CmsConstants.PlatformStatus getpStatus() {
        String pStatus = getStringAttribute("pStatus");
        CmsConstants.PlatformStatus rs = null;
        try {
            rs = (pStatus == null || pStatus.isEmpty()) ? null : CmsConstants.PlatformStatus.valueOf(pStatus);
        } catch (IllegalArgumentException ignored) {
        }
        return rs;
    }

    public void setpStatus(CmsConstants.PlatformStatus pStatus) {
        setAttribute("pStatus", pStatus.name());
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
        return getAttribute(FIELDS);
    }

    /**
     * 返回非空BaseMongoMap对象
     */
    public BaseMongoMap getFieldsNotNull() {
        BaseMongoMap obj = getAttribute(FIELDS);
        if (obj == null) {
            return new BaseMongoMap();
        }
        return obj;
    }

    public void setFields(BaseMongoMap<String, Object> fields) {
        setAttribute(FIELDS, fields);
    }

    //skus
    public List<BaseMongoMap<String, Object>> getSkus() {
        return getAttribute(SKUS);
    }

    public void setSkus(List<BaseMongoMap<String, Object>> skus) {
        setAttribute(SKUS, skus);
    }

    //sellerCats
    public List<CmsBtProductModel_SellerCat> getSellerCats() {
        return getAttribute(SELLER_CATS);
    }

    public void setSellerCats(List<CmsBtProductModel_SellerCat> sellerCats) {
        setAttribute(SELLER_CATS, sellerCats);
    }

    public List<Map<String, Object>> getSellerCatsByMap() {
        return getAttribute(SELLER_CATS);
    }

    public void setSellerCatsByMap(List<Map<String, Object>> sellerCats) {
        setAttribute(SELLER_CATS, sellerCats);
    }

    // mqq
    public List<ProductMqqBean> getMqq() {
        return getAttribute(MQQ);
    }

    public void setMqq(List<ProductMqqBean> mqq) {
        setAttribute(MQQ, mqq);
    }

    public String getpReallyStatus() {
        return getStringAttribute("pReallyStatus");
    }

    public void setpReallyStatus(String pReallyStatus) {
        setStringAttribute("pReallyStatus", pReallyStatus);
    }

    public String getIsNewSku() {
        return getStringAttribute("isNewSku");
    }

    public void setIsNewSku(String isNewSku) {
        setStringAttribute("isNewSku", isNewSku);
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

        // mqq
        if (MQQ.equals(key)) {
            if (value != null) {
                if (value instanceof ProductMqqBean) {
                    List<ProductMqqBean> mmq = new ArrayList<>();
                    mmq.add((ProductMqqBean) value);
                    value = mmq;
                } else if (value instanceof Map) {
                    Map<String, String> map = (Map<String, String>) value;
                    if (map.containsKey("mqqName")) {
                        List<ProductMqqBean> mmq = new ArrayList<>();
                        ProductMqqBean model = new ProductMqqBean();
                        model.setMqqName(map.get("mqqName"));
                        model.setNumIId(map.get("numIId"));
                        try {
                            CmsConstants.PlatformStatus status = (StringUtils.isEmpty(map.get("status"))) ? null : CmsConstants.PlatformStatus.valueOf(map.get("status"));
                            model.setStatus(status);
                        } catch (IllegalArgumentException ignored) {
                        }
                        mmq.add(model);
                        value = mmq;
                    } else {
                        value = null;
                    }
                } else if (value instanceof List) {
                    List<Map<String, Object>> maps = (List<Map<String, Object>>) value;
                    List<ProductMqqBean> listBean = new ArrayList<>();
                    for (Map<String, Object> map : maps) {
                        if (map.containsKey("mqqName")) {
                            ProductMqqBean model = new ProductMqqBean();
                            model.setMqqName((String) map.get("mqqName"));
                            model.setNumIId((String) map.get("numIId"));
                            try {
                                CmsConstants.PlatformStatus status = (StringUtils.isEmpty((String) map.get("status"))) ? null : CmsConstants.PlatformStatus.valueOf((String) map.get("status"));
                                model.setStatus(status);
                            } catch (IllegalArgumentException ignored) {
                            }
                            listBean.add(model);
                        }
                    }
                    value = listBean;
                } else {
                    value = null;
                }
            }
        }

        return super.put(key, value);
    }
}
