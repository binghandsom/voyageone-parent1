package com.voyageone.service.model.cms.mongo.product;

import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
import com.voyageone.base.dao.mongodb.model.ChannelPartitionModel;

/**
 * {@link CmsBtTempCategoryModel} 的商品Model
 *
 * @author linanbin on 6/29/2016
 * @author chuanyu.liang, 12/11/15
 * @version 2.0.0
 * @since 2.2.0
 */
public class CmsBtTempCategoryModel extends ChannelPartitionModel implements Cloneable {

    private Integer id;

    private String urlKey;

    private Integer displayOrder;

    private BaseMongoMap<String, Object> mapping = new BaseMongoMap<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BaseMongoMap<String, Object> getMapping() {
        return mapping;
    }

    public void setMapping(BaseMongoMap<String, Object> mapping) {
        this.mapping = mapping;
    }

    public String getUrlKey() {
        return urlKey;
    }

    public void setUrlKey(String urlKey) {
        this.urlKey = urlKey;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    @Override
    public CmsBtTempCategoryModel clone() throws CloneNotSupportedException {
        return (CmsBtTempCategoryModel) super.clone();
    }
}