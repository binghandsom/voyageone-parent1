package com.voyageone.components.cnn.request;

import com.voyageone.components.cnn.enums.CnnConstants;
import com.voyageone.components.cnn.request.bean.CategoryInfoBean;
import com.voyageone.components.cnn.response.CategoryResetResponse;

import java.util.List;

/**
 * 重置所有店铺内分类，输入参数其实是树形结构
 * Created by morse on 2017/7/31.
 */
public class CategoryResetRequest extends AbstractCnnRequest<CategoryResetResponse> {

    @Override
    public String getUrl() {
        return CnnConstants.CnnApiAction.CATALOG_RESET;
    }

    private List<CategoryInfoBean> catalogList; // 店铺内分类列表(这里是第一级分类)

    public List<CategoryInfoBean> getCatalogList() {
        return catalogList;
    }

    public void setCatalogList(List<CategoryInfoBean> catalogList) {
        this.catalogList = catalogList;
    }
}
