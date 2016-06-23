package com.voyageone.service.model.cms.mongo;

import com.voyageone.base.dao.mongodb.model.CartPartitionModel;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * 平台Schema对应的一些想要重构的属性的表
 * 重构：不想显示表(CmsMtPlatformCategoryInvisibleField)追加field_id='city',本表同步追加field_id='city'
 *
 * @author morse.lu 2016/06/21
 * @version 2.1.0
 * @since 2.1.0
 */
@Document
public class CmsMtPlatformCategoryExtendFieldModel extends CartPartitionModel {

    private String catId;

    private String catFullPath;

    private List<CmsMtPlatformCategoryExtendFieldModel_Field> propsProduct;

    private List<CmsMtPlatformCategoryExtendFieldModel_Field> propsItem;

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

    public List<CmsMtPlatformCategoryExtendFieldModel_Field> getPropsProduct() {
        return propsProduct;
    }

    public void setPropsProduct(List<CmsMtPlatformCategoryExtendFieldModel_Field> propsProduct) {
        this.propsProduct = propsProduct;
    }

    public List<CmsMtPlatformCategoryExtendFieldModel_Field> getPropsItem() {
        return propsItem;
    }

    public void setPropsItem(List<CmsMtPlatformCategoryExtendFieldModel_Field> propsItem) {
        this.propsItem = propsItem;
    }
}