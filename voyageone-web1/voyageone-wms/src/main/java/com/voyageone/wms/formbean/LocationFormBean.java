package com.voyageone.wms.formbean;

import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.StringUtils;
import com.voyageone.wms.modelbean.LocationBean;

/**
 * wms_bt_location 外加 store_name
 * Created by Tester on 5/20/2015.
 *
 * @author Jonas
 */
public class LocationFormBean extends LocationBean {

    private String store_name;
    private String modified_local;
    private String created_local;

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public String getModified_local() {
        return modified_local;
    }

    public void setModified_local(String modified_local) {
        this.modified_local = modified_local;
    }

    public String getCreated_local() {
        return created_local;
    }

    public void setCreated_local(String created_local) {
        this.created_local = created_local;
    }

    /**
     * 直接根据时区格式化 Created 字段到 Created_local
     * @param timeZone 时区
     */
    public void setCreated_local(int timeZone) {

        if (StringUtils.isEmpty(getCreated())) return;

        setCreated_local(DateTimeUtil.getLocalTime(getCreated(), timeZone));

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
