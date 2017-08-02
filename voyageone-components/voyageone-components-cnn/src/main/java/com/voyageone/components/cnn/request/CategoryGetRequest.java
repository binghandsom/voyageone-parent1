package com.voyageone.components.cnn.request;

import com.voyageone.components.cnn.enums.CnnConstants;
import com.voyageone.components.cnn.response.CategoryGetResponse;

/**
 * 查询店铺内分类信息
 * 只返回指定的分类信息及其下级分类信息，（不包括再下一级的分类，若需要请重新指定父节点查询）
 * Created by morse on 2017/7/31.
 */
public class CategoryGetRequest extends CnnUrlRequest<CategoryGetResponse> {

    @Override
    public String getUrl() {
        return CnnConstants.CnnApiAction.CATALOG_SEARCH;
    }

    private String catalogId; // 店铺内分类ID

    public String getCatalogId() {
        return catalogId;
    }

    public void setCatalogId(String catalogId) {
        this.catalogId = catalogId;
    }
}
