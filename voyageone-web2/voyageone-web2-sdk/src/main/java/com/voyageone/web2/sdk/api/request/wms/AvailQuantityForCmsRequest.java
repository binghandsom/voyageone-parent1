package com.voyageone.web2.sdk.api.request.wms;

import com.voyageone.web2.sdk.api.VoApiRequest;
import com.voyageone.web2.sdk.api.exception.ApiRuleException;
import com.voyageone.web2.sdk.api.response.wms.AvailQuantityForCmsResponse;
import com.voyageone.web2.sdk.api.util.RequestUtils;

/**
 * Created by Charis on 2017/5/25.
 */
public class AvailQuantityForCmsRequest extends VoApiRequest<AvailQuantityForCmsResponse>{

    private String channelId;

    private String cartId;

    private String itemCode;

    private String sku;



    @Override
    public String getApiURLPath() {
        return "/wms/stock/cart/getAvailStock";
    }

    @Override
    public void requestCheck() throws ApiRuleException {
        RequestUtils.checkNotEmpty("channelId", channelId);
        RequestUtils.checkNotEmpty("cartId", cartId);
        RequestUtils.checkNotEmpty("itemCode", itemCode);
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

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }
}
