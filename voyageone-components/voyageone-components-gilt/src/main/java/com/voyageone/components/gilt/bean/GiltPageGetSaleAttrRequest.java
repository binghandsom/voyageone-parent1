package com.voyageone.components.gilt.bean;

import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

/**
 * @author aooer 2016/2/3.
 * @version 2.0.0
 * @since 2.0.0
 */
public class GiltPageGetSaleAttrRequest extends GiltPage{

    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void check(){
        Assert.notNull(id);
    }

    public Map<String,String> getBeanMap(){
        Map<String,String> map=new HashMap<String,String>();
        if(getLimit()!=null)
            map.put("limit",getLimit()+"");
        if(getOffset()!=null)
            map.put("offset",getOffset()+"");
        return map;
    }
}
