package com.voyageone.components.gilt.bean;

import org.springframework.util.Assert;

/**
 * @author aooer 2016/2/2.
 * @version 2.0.0
 * @since 2.0.0
 */
public class GiltRealTimeInventory {

    /* The unique identifier of a SKU.*/
    private long sku_id;

    /* The quantity available for this SKU.*/
    private long quantity;

    public long getSku_id() {
        return sku_id;
    }

    public void setSku_id(long sku_id) {
        this.sku_id = sku_id;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    public void check(){
        Assert.notNull(sku_id,"sku_id不能为空");
        Assert.notNull(quantity,"quantity不能为空");
    }

}
