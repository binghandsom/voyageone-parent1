package com.voyageone.wms.formbean;

import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.StringUtils;
import com.voyageone.wms.modelbean.ItemLocationLogBean;

/**
 * wms_bt_item_location_log 外加 location_name
 * Created by Tester on 5/20/2015.
 *
 * @author Jonas
 */
public class ItemLocationLogFormBean extends ItemLocationLogBean {
    private String location_name;

    private String modified_local;

    public String getLocation_name() {
        return location_name;
    }

    public void setLocation_name(String location_name) {
        this.location_name = location_name;
    }

    public String getModified_local() {
        return modified_local;
    }

    public void setModified_local(String modified_local) {
        this.modified_local = modified_local;
    }

    /**
     * 直接根据时区格式化 Modified 字段到 Modified_local
     * @param timeZone 时区
     */
    public void setModified_local(int timeZone) {

        if (StringUtils.isEmpty(getModified())) return;

        setModified_local(DateTimeUtil.getLocalTime(getModified(), timeZone));

    }
}
