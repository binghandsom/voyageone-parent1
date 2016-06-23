package com.voyageone.service.model.cms.mongo;

import com.voyageone.base.dao.mongodb.model.CartPartitionModel;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * 平台Schema对应的不想显示在画面上的属性的表
 * catId=0为全类目共通
 *
 * @author morse.lu 2016/06/21
 * @version 2.1.0
 * @since 2.1.0
 */
@Document
public class CmsMtPlatformCategoryInvisibleFieldModel extends CartPartitionModel {

    private String catId;

    private String catFullPath;

    private List<CmsMtPlatformCategoryInvisibleFieldModel_Field> propsProduct;

    private List<CmsMtPlatformCategoryInvisibleFieldModel_Field> propsItem;

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    public String getCatFullPath() {
        return catFullPath;
    }

    public void setCatFullPath(String catFullPath) {
        this.catFullPath = catFullPath;
    }

    public List<CmsMtPlatformCategoryInvisibleFieldModel_Field> getPropsProduct() {
        return propsProduct;
    }

    public void setPropsProduct(List<CmsMtPlatformCategoryInvisibleFieldModel_Field> propsProduct) {
        this.propsProduct = propsProduct;
    }

    public List<CmsMtPlatformCategoryInvisibleFieldModel_Field> getPropsItem() {
        return propsItem;
    }

    public void setPropsItem(List<CmsMtPlatformCategoryInvisibleFieldModel_Field> propsItem) {
        this.propsItem = propsItem;
    }
}