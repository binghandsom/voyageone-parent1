package com.voyageone.common.magento.api.bean;

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
    String inStock;

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

    public String getInStock() {
        return inStock;
    }

    public void setInStock(String inStock) {
        this.inStock = inStock;
    }
}
