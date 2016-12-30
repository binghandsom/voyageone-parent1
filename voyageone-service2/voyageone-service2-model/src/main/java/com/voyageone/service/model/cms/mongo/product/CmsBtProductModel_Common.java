package com.voyageone.service.model.cms.mongo.product;


import com.voyageone.base.dao.mongodb.model.BaseMongoMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 商品共通属性
 * @author linanbin on 6/29/2016
 * @version 2.2.0
 * @author chuanyu.liang, 2016/06/03
 * @version 2.0.0
 * @since 2.0.0
 */
public class CmsBtProductModel_Common extends BaseMongoMap<String, Object> {

    /** fields **/
    public final static String FIELDS = "fields";
    /** skus **/
    public final static String SKUS = "skus";

    public CmsBtProductModel_Common(){};

    public CmsBtProductModel_Common(Map data){
        putAll(data);
    }

    //主类目ID
    public String getCatId() {
        return getStringAttribute("catId");
    }
    public void setCatId(String catId) {
        setStringAttribute("catId", catId);
    }

    //主类目完整PATH(中文)
    public String getCatPath() {
        return getStringAttribute("catPath");
    }
    public void setCatPath(String catPath) {
        setStringAttribute("catPath", catPath);
    }

    //主类目完整PATH(英文)
    public String getCatPathEn() {
        return getStringAttribute("catPathEn");
    }
    public void setCatPathEn(String catPathEn) {
        setStringAttribute("catPathEn", catPathEn);
    }

    //comment
    public String getComment() {
        return getStringAttribute("comment");
    }
    public void setComment(String comment) {
        setStringAttribute("comment", comment);
    }

    // 更新者
    public String getModifier() {
        return getStringAttribute("modifier");
    }
    public void setModifier(String modifier) {
        setStringAttribute("modifier", modifier);
    }

    // 更新时间
    public String getModified() {
        return getStringAttribute("modified");
    }
    public void setModified(String modified) {
        setStringAttribute("modified", modified);
    }

    // 主类目人工设置FLG
    public String getCatConf() {
        return getStringAttribute("catConf") == null?"0":getStringAttribute("catConf");
    }
    public void setCatConf(String catConf) {
        setStringAttribute("catConf", catConf);
    }

    // fields
    public CmsBtProductModel_Field getFields()  {
        return getAttribute(FIELDS);
    }
    public void setFields(CmsBtProductModel_Field fields) {
        setAttribute(FIELDS, fields);
    }
    /**
     * 返回非空CmsBtProductModel_Field对象
     */
    public CmsBtProductModel_Field getFieldsNotNull()  {
        CmsBtProductModel_Field obj = getAttribute(FIELDS);
        if (obj == null) {
            return new CmsBtProductModel_Field();
        }
        return obj;
    }

    // skus
    public List<CmsBtProductModel_Sku> getSkus() {
        return getAttribute(SKUS);
    }
    public void setSkus(List<CmsBtProductModel_Sku> skus) {
        setAttribute(SKUS, skus);
    }
    /**
     * 根据skuCode返回对应的单个sku数据
     * @param skuCode skuCode
     * @return CmsBtProductModel_CommonSku
     */
    public CmsBtProductModel_Sku getSku(String skuCode) {
        if (skuCode != null && getSkus() != null) {
            for (CmsBtProductModel_Sku sku : getSkus()) {
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
                List<CmsBtProductModel_Sku> skus = new ArrayList<>();
                for (Map<String, Object> map : imageMaps) {
                    if (map != null) {
                        CmsBtProductModel_Sku sku;
                        if (map instanceof CmsBtProductModel_Sku) {
                            sku = (CmsBtProductModel_Sku) map;
                        } else {
                            sku = new CmsBtProductModel_Sku();
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