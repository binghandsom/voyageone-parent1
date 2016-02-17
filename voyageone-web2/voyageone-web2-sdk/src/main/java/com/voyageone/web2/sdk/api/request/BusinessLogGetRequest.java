package com.voyageone.web2.sdk.api.request;

import com.voyageone.web2.sdk.api.VoApiRequest;
import com.voyageone.web2.sdk.api.exception.ApiRuleException;
import com.voyageone.web2.sdk.api.response.BusinessLogGetResponse;

import java.util.List;

/**
 * @author aooer 2016/1/20.
 * @version 2.0.0
 * @since 2.0.0
 */
public class BusinessLogGetRequest extends VoApiRequest<BusinessLogGetResponse> {


    private List<String> codes;

    private String errType;

    private String productName;

    private String channelId;

    private String cartId;

    private String catId;

    private Integer offset = 0;

    private Integer rows = 20;

    @Override
    public String getApiURLPath() {
        return "/businesslog/findlist";
    }

    @Override
    public void requestCheck() throws ApiRuleException {
    }

    public List<String> getCodes() { return codes; }

    public void setCodes(List<String> codes) {
        this.codes = codes;
    }

    public String getErrType() { return errType; }

    public void setErrType(String errType) {
        this.errType = errType;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getCartId() {
        return cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    public Integer getRows() {
        return rows;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }
}
