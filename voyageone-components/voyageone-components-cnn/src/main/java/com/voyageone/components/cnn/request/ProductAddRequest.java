package com.voyageone.components.cnn.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.components.cnn.enums.CnnConstants;
import com.voyageone.components.cnn.request.bean.ProductInfoBean;
import com.voyageone.components.cnn.response.ProductAddResponse;

/**
 * 添加商品
 * Created by morse on 2017/7/31.
 */
public class ProductAddRequest extends AbstractCnnRequest<ProductAddResponse> {

    public ProductAddRequest() {
        productInfoBean = new ProductInfoBean();
    }

    @Override
    public String getUrl() {
        return CnnConstants.CnnApiAction.PRODUCT_ADD;
    }

    private ProductInfoBean productInfoBean;

    @JsonIgnore
    public ProductInfoBean getProductInfoBean() {
        return productInfoBean;
    }

    public void setProductInfoBean(ProductInfoBean productInfoBean) {
        this.productInfoBean = productInfoBean;
    }

    @Override
    public String toJsonStr() {
        return JacksonUtil.bean2Json(productInfoBean);
    }
}
