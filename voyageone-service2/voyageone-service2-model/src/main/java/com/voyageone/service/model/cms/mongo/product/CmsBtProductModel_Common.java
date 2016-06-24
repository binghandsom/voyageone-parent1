package com.voyageone.service.model.cms.mongo.product;


import com.voyageone.base.dao.mongodb.model.BaseMongoMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 商品共通属性
 * @author chuanyu.liang, 2016/06/03
 * @version 2.0.0
 * @since 2.0.0
 */
public class CmsBtProductModel_Common extends BaseMongoMap<String, Object> {

    public final static String FIELDS = "fields";
    public final static String SKUS = "skus";

    public CmsBtProductModel_Common(){};

    public CmsBtProductModel_Common(Map data){
        putAll(data);
    }
    //主类目ID
    public String getCatId() {
        return getAttribute("catId");
    }
    public void setCatId(String catId) {
        setAttribute("catId", catId);
    }

    //主类目完整PATH
    public String getCatPath() {
        return getAttribute("catPath");
    }
    public void setCatPath(String catPath) {
        setAttribute("catPath", catPath);
    }

    public String getModifier() {
        return getAttribute("modifier");
    }
    public void setModifier(String modifier) {
        setAttribute("modifier", modifier);
    }

    public String getModified() {
        return getAttribute("modified");
    }
    public void setModified(String modified) {
        setAttribute("modified", modified);
    }

    public CmsBtProductModel_Field getFields()  {
        return getAttribute("fields");
    }

    /**
     * 返回非空CmsBtProductModel_Field对象，
     */
    public CmsBtProductModel_Field getFieldsNotNull()  {
        CmsBtProductModel_Field obj = getAttribute("fields");
        if (obj == null) {
            return new CmsBtProductModel_Field();
        }
        return obj;
    }

    public void setFields(CmsBtProductModel_Field fields) {
        setAttribute("fields", fields);
    }

    public List<CmsBtProductModel_CommonSku> getSkus() {
        return getAttribute("skus");
    }

    public void setSkus(List<CmsBtProductModel_CommonSku> skus) {
        setAttribute("skus", skus);
    }

    public CmsBtProductModel_CommonSku getSku(String skuCode) {
        if (skuCode != null && getSkus() != null) {
            for (CmsBtProductModel_CommonSku sku : getSkus()) {
                if (skuCode.equals(sku.getSkuCode())) {
                    return sku;
                }
            }
        }
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object put(String key, Object value) {
        // fields
        if (FIELDS.equals(key)) {
            if (value != null) {
                Map<String, Object> map = (Map<String, Object>) value;
                CmsBtProductModel_Field fields;
                if (map instanceof CmsBtProductModel_Field) {
                    fields = (CmsBtProductModel_Field) map;
                } else {
                    fields = new CmsBtProductModel_Field();
                    fields.putAll(map);
                }
                value = fields;
            }
        }

        // skus
        if (SKUS.equals(key)) {
            if (value != null) {
                List<Map<String, Object>> imageMaps = (List<Map<String, Object>>) value;
                List<CmsBtProductModel_CommonSku> skus = new ArrayList<>();
                for (Map<String, Object> map : imageMaps) {
                    if (map != null) {
                        CmsBtProductModel_CommonSku sku;
                        if (map instanceof CmsBtProductModel_CommonSku) {
                            sku = (CmsBtProductModel_CommonSku) map;
                        } else {
                            sku = new CmsBtProductModel_CommonSku();
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