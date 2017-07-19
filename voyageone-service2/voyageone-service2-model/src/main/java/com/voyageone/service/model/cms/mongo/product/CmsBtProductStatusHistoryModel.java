package com.voyageone.service.model.cms.mongo.product;

import com.voyageone.base.dao.mongodb.model.ChannelPartitionModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangjindong on 2017/6/16.
 */
public class CmsBtProductStatusHistoryModel extends ChannelPartitionModel implements Cloneable {
    private String channelId;
    // cart_id
    private int cartId;
    private String code;
    private List<CmsBtProductStatusHistoryModel_History> list = new ArrayList<>();

    @Override
    public String getChannelId() {
        return channelId;
    }

    @Override
    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<CmsBtProductStatusHistoryModel_History> getList() {
        return list;
    }

    public void setList(List<CmsBtProductStatusHistoryModel_History> list) {
        this.list = list;
    }
}
