package com.voyageone.components.cn.enums;

/**
 * 库存同步类型
 *
 * Created by Jack on 6/18/2015.
 */
public enum InventorySynType {
    /**
     * 刷新所有商品的库存
     */
    FULL(0),
    /**
     * 只更新 log 中提及的库存变更
     */
    INCREMENT(1);

    private int val;

    InventorySynType(int val) {
        this.val = val;
    }

    public int val() {
        return val;
    }
}
