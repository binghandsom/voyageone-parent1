package com.voyageone.cms.service.model;


import com.voyageone.base.dao.mongodb.model.BaseMongoMap;

import java.util.List;
import java.util.Map;

/**
 * 的商品Model Field>Image
 * @author chuanyu.liang, 12/11/15
 * @version 2.0.0
 * @since 2.0.0
 */
public class CmsBtProductModel_Field_Image extends BaseMongoMap<String, Object> {

    public CmsBtProductModel_Field_Image() {
    }

    public CmsBtProductModel_Field_Image(String name) {
        setAttribute("name", name);
    }

    public CmsBtProductModel_Field_Image(Map map) {
        this.putAll(map);
    }

    public String getName() {
        return getAttribute("name");
    }

    public void setName(String name) {
        setAttribute("name", name);
    }

}