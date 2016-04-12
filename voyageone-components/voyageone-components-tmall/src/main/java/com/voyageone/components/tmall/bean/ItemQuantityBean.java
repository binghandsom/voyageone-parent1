package com.voyageone.components.tmall.bean;

/**
 * 用于提供调用淘宝库存更新 API 的数据提供类
 *
 * Created by jonas on 15/6/4.
 */
public class ItemQuantityBean {
    private long num_iid;
    private String sku;
    private long quantity;
    private boolean isTotal;

    public long getNum_iid() {
        return num_iid;
    }

    public void setNum_iid(long num_iid) {
        this.num_iid = num_iid;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    public boolean isTotal() {
        return isTotal;
    }

    public void setIsTotal(boolean isTotal) {
        this.isTotal = isTotal;
    }
}
