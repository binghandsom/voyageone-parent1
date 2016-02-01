package com.voyageone.common.components.gilt.bean;

import com.sun.istack.internal.NotNull;
import com.taobao.api.internal.util.RequestCheckUtils;
import com.voyageone.common.util.StringUtils;
import org.springframework.util.Assert;

/**
 * 实时库存
 * @author aooer 2016/2/1.
 * @version 2.0.0
 * @since 2.0.0
 */
public class RealTimeInventory {

    /* The unique identifier of a SKU.*/
    private long skuId;

    /* The quantity available for this SKU.*/
    private long quantity;

    public long getSkuId() {
        return skuId;
    }

    public void setSkuId(long skuId) {
        this.skuId = skuId;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    private void check(){
        Assert.notNull(skuId,"skuId不能为空");
        Assert.notNull(quantity,"quantity不能为空");
    }
}
