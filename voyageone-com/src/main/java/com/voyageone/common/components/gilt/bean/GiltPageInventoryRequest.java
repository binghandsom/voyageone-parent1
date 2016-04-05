package com.voyageone.common.components.gilt.bean;

import com.voyageone.common.util.StringUtils;
import org.springframework.util.Assert;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author aooer 2016/2/3.
 * @version 2.0.0
 * @since 2.0.0
 */
public class GiltPageInventoryRequest extends GiltPage{

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

    public Map<String,String> getBeanMap(){
        Map<String,String> map=new HashMap<String,String>();
        if(since!=null)
            map.put("since",new SimpleDateFormat().format(since));
        if(!StringUtils.isNullOrBlank2(sku_ids))
            map.put("sku_ids",sku_ids);
        if(getLimit()!=null)
            map.put("limit",getLimit()+"");
        if(getOffset()!=null)
            map.put("offset",getOffset()+"");
        return map;
    }

    public void check(){
        if(!StringUtils.isNullOrBlank2(sku_ids) && sku_ids.split(",").length > 100) {
            throw new IllegalArgumentException(" A comma delineated array of Sku ids to fetch. (Max 100)");
        }
    }
}
