package com.voyageone.components.cnn.request;

import com.voyageone.components.cnn.enums.CnnConstants;
import com.voyageone.components.cnn.response.CategoryProductSetResponse;

import java.util.List;

/**
 * 批量设置商品的店铺内分类
 * numIId设置为idList中的店铺内分类，直接完全覆盖
 * Created by morse on 2017/7/31.
 */
public class CategoryProductSetRequest extends AbstractCnnRequest<CategoryProductSetResponse> {

    @Override
    public String getUrl() {
        return CnnConstants.CnnApiAction.CATALOG_PRODUCT_SET;
    }

    private Long numIId; // 商品numIId
    private List<String> idList; // 店铺内分类ID列表，最多10个，目前必须是叶子节点

    public Long getNumIId() {
        return numIId;
    }

    public void setNumIId(Long numIId) {
        this.numIId = numIId;
    }

    public List<String> getIdList() {
        return idList;
    }

    public void setIdList(List<String> idList) {
        this.idList = idList;
    }

}
