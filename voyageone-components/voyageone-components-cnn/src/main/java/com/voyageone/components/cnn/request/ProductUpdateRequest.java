package com.voyageone.components.cnn.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.components.cnn.enums.CnnConstants;
import com.voyageone.components.cnn.request.bean.ProductInfoBean;
import com.voyageone.components.cnn.response.ProductUpdateResponse;

import java.util.Map;

/**
 * 更新商品信息（单个商品）
 * Created by morse on 2017/7/31.
 */
public class ProductUpdateRequest extends AbstractCnnRequest<ProductUpdateResponse> {

    public ProductUpdateRequest() {
        productInfoBean = new ProductInfoBean();
    }

    @Override
    public String getUrl() {
        return CnnConstants.CnnApiAction.PRODUCT_UPDATE;
    }

    private long numIId;
    private ProductInfoBean productInfoBean;

    public long getNumIId() {
        return numIId;
    }

    public void setNumIId(long numIId) {
        this.numIId = numIId;
    }

    @JsonIgnore
    public ProductInfoBean getProductInfoBean() {
        return productInfoBean;
    }

    public void setProductInfoBean(ProductInfoBean productInfoBean) {
        this.productInfoBean = productInfoBean;
    }

    @Override
    public String toJsonStr() {
        Map<String, Object> map = JacksonUtil.bean2Map(productInfoBean);
        map.putAll(JacksonUtil.bean2Map(this));
        return JacksonUtil.bean2Json(map);
    }

}
