package com.voyageone.components.overstock.bean;

/**
 * @author aooer 2016/6/7.
 * @version 2.0.0
 * @since 2.0.0
 */
public class OverstockMultipleRequest {

    private int offset = 0;

    private int limit = 10;


    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
}
