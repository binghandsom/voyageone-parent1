package com.voyageone.components.cnn.request;

import com.voyageone.components.cnn.enums.CnnConstants;
import com.voyageone.components.cnn.response.CategoryAddResponse;

/**
 * 添加店铺内分类
 * Created by morse on 2017/7/31.
 */
public class CategoryAddRequest extends AbstractCnnRequest<CategoryAddResponse> {

    @Override
    public String getUrl() {
        return CnnConstants.CnnApiAction.CATALOG_ADD;
    }

    private String id; // 店铺内分类ID
    private String parentCatalogId; // 该分类的父节点ID(为'0'时表示是顶级节点)
    private String name; // 店铺内分类名称

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentCatalogId() {
        return parentCatalogId;
    }

    public void setParentCatalogId(String parentCatalogId) {
        this.parentCatalogId = parentCatalogId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
