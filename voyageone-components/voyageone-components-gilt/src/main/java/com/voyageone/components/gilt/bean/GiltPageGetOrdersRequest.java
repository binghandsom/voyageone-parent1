package com.voyageone.components.gilt.bean;

import com.voyageone.common.util.StringUtils;

import java.util.Date;

/**
 * @author aooer 2016/2/2.
 * @version 2.0.0
 * @since 2.0.0
 */
public class GiltPageGetOrdersRequest extends GiltPage {

    /* (DEPRECATED) Filter by modified since in ISO8601Format */
    private Date since;

    /* A comma delineated array of Order ids to fetch. (Max 100)*/
    private String order_ids;

    public Date getSince() {
        return since;
    }

    public void setSince(Date since) {
        this.since = since;
    }

    public String getOrder_ids() {
        return order_ids;
    }

    public void setOrder_ids(String order_ids) {
        this.order_ids = order_ids;
    }

    public void check(){
        if(!StringUtils.isNullOrBlank2(order_ids) && order_ids.split(",").length > 100) {
            throw new IllegalArgumentException(" A comma delineated array of Order ids to fetch. (Max 100)");
        }
    }
}
