package com.voyageone.service.bean.wms;

/**
 * @description
 * @author: holysky.zhao
 * @date: 2016/11/16 14:04
 * @version:1.0.0 COPYRIGHT © 2001 - 2016 VOYAGE ONE GROUP INC. ALL RIGHTS RESERVED.
 */
public class InventoryCenterBean {


    /**
     * seq : 1736
     * order_channel_id : 008
     * store_id : 11
     * code : AFEPCRSREA
     * sku : AFEPCRSREA-OneSize
     * qty : 923
     * is_shared : 1
     * active : 1
     * created : 2015-07-17 10:48:00
     * creater : WmsSetClientInventoryJob
     * modified : 2016-10-20 22:54:02
     * modifier : WmsSetClientInventoryJob
     */

    private int seq;
    private String order_channel_id;
    private int store_id;
    private String code;
    private String sku;
    private int qty;
    private String is_shared;
    private int active;
    private String created;
    private String creater;
    private String modified;
    private String modifier;

    public int getSeq() { return seq;}

    public void setSeq(int seq) { this.seq = seq;}

    public String getOrder_channel_id() { return order_channel_id;}

    public void setOrder_channel_id(String order_channel_id) { this.order_channel_id = order_channel_id;}

    public int getStore_id() { return store_id;}

    public void setStore_id(int store_id) { this.store_id = store_id;}

    public String getCode() { return code;}

    public void setCode(String code) { this.code = code;}

    public String getSku() { return sku;}

    public void setSku(String sku) { this.sku = sku;}

    public int getQty() { return qty;}

    public void setQty(int qty) { this.qty = qty;}

    public String getIs_shared() { return is_shared;}

    public void setIs_shared(String is_shared) { this.is_shared = is_shared;}

    public int getActive() { return active;}

    public void setActive(int active) { this.active = active;}

    public String getCreated() { return created;}

    public void setCreated(String created) { this.created = created;}

    public String getCreater() { return creater;}

    public void setCreater(String creater) { this.creater = creater;}

    public String getModified() { return modified;}

    public void setModified(String modified) { this.modified = modified;}

    public String getModifier() { return modifier;}

    public void setModifier(String modifier) { this.modifier = modifier;}
}
