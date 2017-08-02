package com.voyageone.components.cnn.request;

import com.voyageone.components.cnn.enums.CnnConstants;
import com.voyageone.components.cnn.response.CategoryUpdateNameResponse;

/**
 * 修改店铺内分类名称
 * Created by morse on 2017/7/31.
 */
public class CategoryUpdateNameRequest extends AbstractCnnRequest<CategoryUpdateNameResponse> {

    @Override
    public String getUrl() {
        return CnnConstants.CnnApiAction.CATALOG_UPDATE_NAME;
    }

    private String id; // 店铺内分类ID
    private String name; // 店铺内分类名称

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
