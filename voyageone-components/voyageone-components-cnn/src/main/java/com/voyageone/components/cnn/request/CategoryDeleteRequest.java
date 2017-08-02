package com.voyageone.components.cnn.request;

import com.voyageone.components.cnn.enums.CnnConstants;
import com.voyageone.components.cnn.response.CategoryDeleteResponse;

/**
 * 删除店铺内分类
 * Created by morse on 2017/7/31.
 */
public class CategoryDeleteRequest extends AbstractCnnRequest<CategoryDeleteResponse> {

    @Override
    public String getUrl() {
        return CnnConstants.CnnApiAction.CATALOG_DELETE;
    }

    private String id; // 店铺内分类ID

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
