package com.voyageone.wms.modelbean;

import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.StringUtils;

/**
 * Created by Tester on 4/30/2015.
 *
 * @author Jonas
 */
public class TransferDetailBean {
    private long transfer_package_id;
    private long transfer_id;
    private long transfer_package_qty;
    private String transfer_package_name;
    private String package_status;
    private boolean active;
    private String created;
    private String creater;
    private String modified;
    private String modifier;

    private String modified_local;

    public long getTransfer_package_qty() {
        return transfer_package_qty;
    }

    public void setTransfer_package_qty(long transfer_package_qty) {
        this.transfer_package_qty = transfer_package_qty;
    }

    public long getTransfer_package_id() {
        return transfer_package_id;
    }

    public void setTransfer_package_id(long transfer_package_id) {
        this.transfer_package_id = transfer_package_id;
    }

    public long getTransfer_id() {
        return transfer_id;
    }

    public void setTransfer_id(long transfer_id) {
        this.transfer_id = transfer_id;
    }

    public String getTransfer_package_name() {
        return transfer_package_name;
    }

    public void setTransfer_package_name(String transfer_package_name) {
        this.transfer_package_name = transfer_package_name;
    }

    public String getPackage_status() {
        return package_status;
    }

    public void setPackage_status(String package_status) {
        this.package_status = package_status;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public String getModified_local() {
        return modified_local;
    }

    public void setModified_local(String modified_local) {
        this.modified_local = modified_local;
    }

    public void setModified_local(int timeZone) {
        if (StringUtils.isEmpty(getModified())) return;

        setModified_local(DateTimeUtil.getLocalTime(getModified(), timeZone));
    }
}
