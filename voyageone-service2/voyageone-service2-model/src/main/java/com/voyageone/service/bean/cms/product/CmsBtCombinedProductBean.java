package com.voyageone.service.bean.cms.product;

import com.voyageone.service.model.cms.mongo.product.CmsBtCombinedProductModel;

import java.util.List;

/**
 * Created by rex.wu on 2016/11/29.
 */
public class CmsBtCombinedProductBean extends CmsBtCombinedProductModel {
    private String numIDs; // 多个商品编码，以换行符分割
    private String skuCodes; // 多项查询,精确查询，包括:单品sku，组合套装sku(多项换行分割)
    private List<Integer> statuses; // 组合套装状态，多值
    private List<Integer> platformStatuses; // 组合套装平台状态：多值

    private int size;
    private int total;
    private int curr;

    public String getNumIDs() {
        return numIDs;
    }

    public void setNumIDs(String numIDs) {
        this.numIDs = numIDs;
    }

    public String getSkuCodes() {
        return skuCodes;
    }

    public void setSkuCodes(String skuCodes) {
        this.skuCodes = skuCodes;
    }

    public List<Integer> getStatuses() {
        return statuses;
    }

    public void setStatuses(List<Integer> statuses) {
        this.statuses = statuses;
    }

    public List<Integer> getPlatformStatuses() {
        return platformStatuses;
    }

    public void setPlatformStatuses(List<Integer> platformStatuses) {
        this.platformStatuses = platformStatuses;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getCurr() {
        return curr;
    }

    public void setCurr(int curr) {
        this.curr = curr;
    }
}
