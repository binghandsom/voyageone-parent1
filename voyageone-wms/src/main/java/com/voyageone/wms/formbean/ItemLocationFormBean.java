package com.voyageone.wms.formbean;

import com.voyageone.wms.modelbean.ItemLocationBean;

/**
 * wms_bt_item_location 外加 location_name
 * Created by Tester on 5/20/2015.
 *
 * @author Jonas
 */
public class ItemLocationFormBean extends ItemLocationBean {
    private String location_name;

    public String getLocation_name() {
        return location_name;
    }

    public void setLocation_name(String location_name) {
        this.location_name = location_name;
    }
}
