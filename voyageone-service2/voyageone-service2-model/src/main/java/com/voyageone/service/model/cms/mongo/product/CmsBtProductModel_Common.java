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

    public CmsBtProductModel_Field getFields() {
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
}