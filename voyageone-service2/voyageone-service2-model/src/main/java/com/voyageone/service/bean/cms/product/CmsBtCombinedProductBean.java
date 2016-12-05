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

    private int pageSize;
    private int total;
    private int page;
    private int totalPages;

    public String getNumIDs() {
        return numIDs;
    }

    public void setNumIDs(String numIDs) {
        this.numIDs = numIDs;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public List<Integer> getPlatformStatuses() {
        return platformStatuses;
    }

    public void setPlatformStatuses(List<Integer> platformStatuses) {
        this.platformStatuses = platformStatuses;
    }

    public List<Integer> getStatuses() {
        return statuses;
    }

    public void setStatuses(List<Integer> statuses) {
        this.statuses = statuses;
    }

    public String getSkuCodes() {
        return skuCodes;
    }

    public void setSkuCodes(String skuCodes) {
        this.skuCodes = skuCodes;
    }
}
