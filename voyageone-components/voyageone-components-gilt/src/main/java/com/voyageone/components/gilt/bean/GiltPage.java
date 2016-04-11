package com.voyageone.components.gilt.bean;

/**
 * @author aooer 2016/2/2.
 * @version 2.0.0
 * @since 2.0.0
 *
 *  example sql: select id from table offset,limit
 */
public class GiltPage {

    /* pageSize */
    private Integer limit;

    /* pageStart */
    private Integer offset;

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

}
