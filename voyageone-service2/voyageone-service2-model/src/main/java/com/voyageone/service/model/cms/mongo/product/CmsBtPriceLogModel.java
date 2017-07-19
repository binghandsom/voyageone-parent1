package com.voyageone.service.model.cms.mongo.product;

import com.voyageone.base.dao.mongodb.model.ChannelPartitionModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangjindong on 2017/6/21.
 */
public class CmsBtPriceLogModel extends ChannelPartitionModel implements Cloneable {
    private String channelId;
    private long productId;
    // cart_id
    private Integer cartId;
    private String code;
    private String sku;
    private List<CmsBtPriceLogModel_History> list = new ArrayList<>();
    
    @Override
    public String getChannelId() {
        return channelId;
    }

    @Override
    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public Integer getCartId() {
        return cartId;
    }

    public void setCartId(Integer cartId) {
        this.cartId = cartId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public List<CmsBtPriceLogModel_History> getList() {
        return list;
    }

    public void setList(List<CmsBtPriceLogModel_History> list) {
        this.list = list;
    }
}
