package com.voyageone.components.cn.enums;

/**
 * 独立域名上新类型
 *
 * Created by Jack on 6/18/2015.
 */
public enum CnUpdateType {
    /**
     * 刷新所有商品的库存
     */
    INVENTORY_FULL(0),
    /**
     * 商品的新增或更新
     */
    PRODUCT(1),
    /**
     * 设置类目里的商品的排序
     */
    CATEGORY_PRODUCT(2),
    /**
     * category的创建或更新
     */
    CATEGORY(3),
    /**
     * 只更新 log 中提及的库存变更
     */
    INCREMENT(4);

    private int val;

    CnUpdateType(int val) {
        this.val = val;
    }

    public int val() {
        return val;
    }
}
