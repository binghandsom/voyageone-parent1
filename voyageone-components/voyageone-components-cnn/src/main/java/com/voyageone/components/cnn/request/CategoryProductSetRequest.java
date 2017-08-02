package com.voyageone.components.cnn.request;

import com.voyageone.components.cnn.enums.CnnConstants;
import com.voyageone.components.cnn.response.CategoryProductSetResponse;

import java.util.List;

/**
 * 批量设置商品的店铺内分类
 * numIIdList中的每一个商品都设置为idList中的店铺内分类，直接完全覆盖
 * Created by morse on 2017/7/31.
 */
public class CategoryProductSetRequest extends AbstractCnnRequest<CategoryProductSetResponse> {

    @Override
    public String getUrl() {
        return CnnConstants.CnnApiAction.CATALOG_PRODUCT_SET;
    }

    private List<String> idList; // 店铺内分类ID列表，最多10个，目前必须是叶子节点
    private List<Long> numIIdList; // 商品numIId列表，最多500个

    public List<String> getIdList() {
        return idList;
    }

    public void setIdList(List<String> idList) {
        this.idList = idList;
    }

    public List<Long> getNumIIdList() {
        return numIIdList;
    }

    public void setNumIIdList(List<Long> numIIdList) {
        this.numIIdList = numIIdList;
    }
}
