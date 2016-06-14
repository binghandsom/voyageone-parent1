package com.voyageone.service.model.cms.mongo.product;


import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.CmsConstants;
import org.apache.commons.lang3.StringUtils;

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

    public CmsBtProductModel_Field getFields()  {
        return getAttribute("fields");
    }

    public void setFields(CmsBtProductModel_Field fields) {
        setAttribute("fields", fields);
    }

    public List<CmsBtProductModel_Sku> getSkus() {
        return getAttribute("skus");
    }

    public void setSkus(List<CmsBtProductModel_Sku> skus) {
        setAttribute("skus", skus);
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