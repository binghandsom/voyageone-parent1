package com.voyageone.base.dao.mongodb.model;

/**
 * ChannelPartitionModel
 * @author chuanyu.liang, 12/11/15
 * @version 2.0.0
 * @since 2.0.0
 */
public class CartPartitionModel extends BaseMongoModel {

    protected int cartId;

    public CartPartitionModel() {
    }

    public CartPartitionModel(int cartId) {
        this.cartId = cartId;
    }

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }
}
