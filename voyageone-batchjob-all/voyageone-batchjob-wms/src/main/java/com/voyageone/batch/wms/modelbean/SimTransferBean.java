package com.voyageone.batch.wms.modelbean;

/**
 * @author jack
 * 模拟入出库Bean
 */
public class SimTransferBean {

    /**
     * 处理ID
     */
    private String process_id;

    /**
     * 订单渠道
     */
    private String order_channel_id;

    /**
     * 仓库ID
     */
    private String store_id;

    /**
     * 入出库状态
     */
    private String transfer_status;

    /**
     * 入出库类型
     */
    private String transfer_type;

    /**
     * 入出库名称
     */
    private String transfer_name;

    /**
     * PO番号
     */
    private String po_number;

    /**
     * from仓库
     */
    private String transfer_from_store;

    /**
     * to仓库
     */
    private String transfer_to_store;

    /**
     * 入出库来源
     */
    private String transfer_origin;

    /**
     * 来源ID
     */
    private String origin_id;

    /**
     * 模拟标志位
     */
    private String sim_flg;

    /**
     * 备注
     */
    private String comment;

    /**
     * 有效
     */
    private String active;

    /**
     * 错误信息
     */
    private String errorInfo;

    public String getProcess_id() {
        return process_id;
    }

    public void setProcess_id(String process_id) {
        this.process_id = process_id;
    }

    public String getOrder_channel_id() {
        return order_channel_id;
    }

    public void setOrder_channel_id(String order_channel_id) {
        this.order_channel_id = order_channel_id;
    }

    public String getStore_id() {
        return store_id;
    }

    public void setStore_id(String store_id) {
        this.store_id = store_id;
    }

    public String getTransfer_status() {
        return transfer_status;
    }

    public void setTransfer_status(String transfer_status) {
        this.transfer_status = transfer_status;
    }

    public String getTransfer_type() {
        return transfer_type;
    }

    public void setTransfer_type(String transfer_type) {
        this.transfer_type = transfer_type;
    }

    public String getTransfer_name() {
        return transfer_name;
    }

    public void setTransfer_name(String transfer_name) {
        this.transfer_name = transfer_name;
    }

    public String getPo_number() {
        return po_number;
    }

    public void setPo_number(String po_number) {
        this.po_number = po_number;
    }

    public String getTransfer_from_store() {
        return transfer_from_store;
    }

    public void setTransfer_from_store(String transfer_from_store) {
        this.transfer_from_store = transfer_from_store;
    }

    public String getTransfer_to_store() {
        return transfer_to_store;
    }

    public void setTransfer_to_store(String transfer_to_store) {
        this.transfer_to_store = transfer_to_store;
    }

    public String getTransfer_origin() {
        return transfer_origin;
    }

    public void setTransfer_origin(String transfer_origin) {
        this.transfer_origin = transfer_origin;
    }

    public String getSim_flg() {
        return sim_flg;
    }

    public void setSim_flg(String sim_flg) {
        this.sim_flg = sim_flg;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getOrigin_id() {
        return origin_id;
    }

    public void setOrigin_id(String origin_id) {
        this.origin_id = origin_id;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getErrorInfo() {
        return errorInfo;
    }

    public void setErrorInfo(String errorInfo) {
        this.errorInfo = errorInfo;
    }
}
