package com.voyageone.components.cnn.request;

import com.voyageone.components.cnn.enums.CnnConstants;
import com.voyageone.components.cnn.response.CategoryProductResetResponse;

import java.util.List;

/**
 * 重置指定店铺内分类下的所有商品
 * Created by morse on 2017/7/31.
 */
public class CategoryProductResetRequest extends AbstractCnnRequest<CategoryProductResetResponse> {

    @Override
    public String getUrl() {
        return CnnConstants.CnnApiAction.CATALOG_PRODUCT_RESET;
    }

    private String catId; // 店铺内分类ID
    private List<Long> numIIdList; // 商品numIId列表，最多500个

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    public List<Long> getNumIIdList() {
        return numIIdList;
    }

    public void setNumIIdList(List<Long> numIIdList) {
        this.numIIdList = numIIdList;
    }
}
