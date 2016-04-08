package com.voyageone.components.tmall.bean;

import java.util.Map;

/**
 * 用于提供调用淘宝多物品库存批量更新 API 的数据提供类
 *
 * Created by jonas on 15/6/4.
 */
public class SkusQuantityBean {
    private long num_iid;
    private boolean total;
    private Map<String, Integer> skuQuantities;

    public long getNum_iid() {
        return num_iid;
    }

    public void setNum_iid(long num_iid) {
        this.num_iid = num_iid;
    }

    public boolean isTotal() {
        return total;
    }

    public void setTotal(boolean total) {
        this.total = total;
    }

    public Map<String, Integer> getSkuQuantities() {
        return skuQuantities;
    }

    public void setSkuQuantities(Map<String, Integer> skuQuantities) {
        this.skuQuantities = skuQuantities;
    }
}
