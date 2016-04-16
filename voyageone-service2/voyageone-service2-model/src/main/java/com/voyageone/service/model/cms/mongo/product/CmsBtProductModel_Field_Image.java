package com.voyageone.service.model.cms.mongo.product;


import com.voyageone.base.dao.mongodb.model.BaseMongoMap;

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

    public CmsBtProductModel_Field_Image(String key, String name) {
        // 20160121 tom 结构调整 START
        // 原本这边定下的名字是name, 但是发现刘耀的主数据的schema里的名字有些是image1, 有些是image2……,
        // 所以这个name只能作为变量传进来
//        setAttribute("name", name);

        setAttribute(key, name);
        // 20160121 tom 结构调整 END
    }

    public CmsBtProductModel_Field_Image(Map map) {
        this.putAll(map);
    }

    public String getName() {
        // 20160121 tom 结构调整 START
        // 原本这边定下的名字是name, 但是发现刘耀的主数据的schema里的名字有些是image1, 有些是image2……
//        return getAttribute("name");
        if (this.keySet().iterator().hasNext()) {
            return this.get(this.keySet().iterator().next()).toString();
        } else {
            return  "";
        }
        // 20160121 tom 结构调整 END
    }

    public void setName(String key, String name) {
        // 20160121 tom 结构调整 START
        // 原本这边定下的名字是name, 但是发现刘耀的主数据的schema里的名字有些是image1, 有些是image2……,
        // 所以这个name只能作为变量传进来
//        setAttribute("name", name);

        setAttribute(key, name);
        // 20160121 tom 结构调整 END
    }

}