package com.voyageone.wms.formbean;

import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.StringUtils;
import com.voyageone.wms.modelbean.TransferBean;

/**
 * Transfer 将数字值扩展为名称
 * <p/>
 * Created by jonas on 15/6/11.
 */
public class TransferFormBean extends TransferBean {

    private String store_name;

    private String transfer_type_name;

    private String transfer_status_name;

    private String itemcode;

    private String size;

    private int transfer_qty;

    private String details_num;

    private String modified_local;

    public String getStore_name() {
        return store_name;
    }

    protected void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public String getTransfer_type_name() {
        return transfer_type_name;
    }

    protected void setTransfer_type_name(String transfer_type_name) {
        this.transfer_type_name = transfer_type_name;
    }

    public String getTransfer_status_name() {
        return transfer_status_name;
    }

    protected void setTransfer_status_name(String transfer_status_name) {
        this.transfer_status_name = transfer_status_name;
    }

    public String getItemcode() {
        return itemcode;
    }

    public void setItemcode(String itemcode) {
        this.itemcode = itemcode;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public int getTransfer_qty() {
        return transfer_qty;
    }

    public void setTransfer_qty(int transfer_qty) {
        this.transfer_qty = transfer_qty;
    }

    public String getDetails_num() {
        return details_num;
    }

    public void setDetails_num(String details_num) {
        this.details_num = details_num;
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
