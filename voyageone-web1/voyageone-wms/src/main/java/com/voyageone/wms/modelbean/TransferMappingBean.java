package com.voyageone.wms.modelbean;

/**
 * Created by Tester on 5/5/2015.
 *
 * @author Jonas
 */
public class TransferMappingBean {
    private long transfer_in_id;
    private long transfer_out_id;
    private boolean mapping_status;
    private boolean active;
    private String created;
    private String creater;
    private String modified;
    private String modifier;

    public long getTransfer_in_id() {
        return transfer_in_id;
    }

    public void setTransfer_in_id(long transfer_in_id) {
        this.transfer_in_id = transfer_in_id;
    }

    public long getTransfer_out_id() {
        return transfer_out_id;
    }

    public void setTransfer_out_id(long transfer_out_id) {
        this.transfer_out_id = transfer_out_id;
    }

    public boolean isMapping_status() {
        return mapping_status;
    }

    public void setMapping_status(boolean mapping_status) {
        this.mapping_status = mapping_status;
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
}
