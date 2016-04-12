package com.voyageone.components.gilt.bean;

/**
 *
 * Created by sky on 20150729.
 */
public class GetInventoryParamBean {

    int nPageSize;
    int nPageIndex;
    String dateTimeSince;
    int updateCount;
    String storeArea;
    // 是否以Sales更新
    String salesAllow;
    // Sales更新间隔
    String salesInterval;
    // Sales更新前次时间
    String salesUpdateSince;
    // Sales更新类型
    String salesUpdateType;
    // 是否全量库存更新
    String fullAllow;
    // Sales限制件数
    String salesLimit;

    public int getnPageSize() {
        return nPageSize;
    }

    public void setnPageSize(int nPageSize) {
        this.nPageSize = nPageSize;
    }

    public int getnPageIndex() {
        return nPageIndex;
    }

    public void setnPageIndex(int nPageIndex) {
        this.nPageIndex = nPageIndex;
    }

    public String getDateTimeSince() {
        return dateTimeSince;
    }

    public void setDateTimeSince(String dateTimeSince) {
        this.dateTimeSince = dateTimeSince;
    }

    public int getUpdateCount() {
        return updateCount;
    }

    public void setUpdateCount(int updateCount) {
        this.updateCount = updateCount;
    }

    public String getStoreArea() {
        return storeArea;
    }

    public void setStoreArea(String storeArea) {
        this.storeArea = storeArea;
    }

    public String getSalesAllow() {
        return salesAllow;
    }

    public void setSalesAllow(String salesAllow) {
        this.salesAllow = salesAllow;
    }

    public String getSalesInterval() {
        return salesInterval;
    }

    public void setSalesInterval(String salesInterval) {
        this.salesInterval = salesInterval;
    }

    public String getSalesUpdateSince() {
        return salesUpdateSince;
    }

    public void setSalesUpdateSince(String salesUpdateSince) {
        this.salesUpdateSince = salesUpdateSince;
    }

    public String getSalesUpdateType() {
        return salesUpdateType;
    }

    public void setSalesUpdateType(String salesUpdateType) {
        this.salesUpdateType = salesUpdateType;
    }

    public String getFullAllow() {
        return fullAllow;
    }

    public void setFullAllow(String fullAllow) {
        this.fullAllow = fullAllow;
    }

    public String getSalesLimit() {
        return salesLimit;
    }

    public void setSalesLimit(String salesLimit) {
        this.salesLimit = salesLimit;
    }
}
