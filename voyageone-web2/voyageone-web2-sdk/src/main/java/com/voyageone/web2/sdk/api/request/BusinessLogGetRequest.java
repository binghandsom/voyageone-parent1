package com.voyageone.web2.sdk.api.request;

import com.voyageone.web2.sdk.api.VoApiRequest;
import com.voyageone.web2.sdk.api.exception.ApiRuleException;
import com.voyageone.web2.sdk.api.response.BusinessLogGetResponse;
import com.voyageone.web2.sdk.api.util.RequestUtils;

import java.util.Arrays;
import java.util.List;

/**
 * @author aooer 2016/1/20.
 * @version 2.0.0
 * @since 2.0.0
 */
public class BusinessLogGetRequest extends VoApiRequest<BusinessLogGetResponse> {


    private List<Integer> productIds;

    private String errType;

    private String productName;

    private Integer cartId;

    private String catId;


    @Override
    public String getApiURLPath() {
        return "/businesslog/findlist";
    }

    @Override
    public void requestCheck() throws ApiRuleException {
    }

    public List<Integer> getProductIds() {
        return productIds;
    }

    public void setProductIds(List<Integer> productIds) {
        this.productIds = productIds;
    }

    /*public void setProductIds(Integer... productId) {
        this.productIds = Arrays.asList(productId);
    }*/

    public String getErrType() {
        return errType;
    }

    public void setErrType(String errType) {
        this.errType = errType;
    }


    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getCartId() {
        return cartId;
    }

    public void setCartId(Integer cartId) {
        this.cartId = cartId;
    }

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }
}
