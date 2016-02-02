package com.voyageone.common.components.gilt.bean;

import java.util.Date;

/**
 * @author aooer 2016/2/2.
 * @version 2.0.0
 * @since 2.0.0
 */
public class GiltPageGetSkusRequest extends GiltPage{

    /* (DEPRECATED) Filter by modified since in ISO8601Format */
    private Date since;

    /* A comma delineated array of Sku ids to fetch. (Max 100)*/
    private String sku_ids;

    public Date getSince() {
        return since;
    }

    public void setSince(Date since) {
        this.since = since;
    }

    public String getSku_ids() {
        return sku_ids;
    }

    public void setSku_ids(String sku_ids) {
        this.sku_ids = sku_ids;
    }
}
