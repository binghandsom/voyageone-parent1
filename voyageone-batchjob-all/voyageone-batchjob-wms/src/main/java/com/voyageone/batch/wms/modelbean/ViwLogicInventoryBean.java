package com.voyageone.batch.wms.modelbean;

/**
 * Created by jonas on 15/6/9.
 */
public class ViwLogicInventoryBean {
    private String code;
    private String sku;
    private int qty_china_logic;
    private int qty_orgin_logi;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public int getQty_china_logic() {
        return qty_china_logic;
    }

    public void setQty_china_logic(int qty_china_logic) {
        this.qty_china_logic = qty_china_logic;
    }

    public int getQty_orgin_logi() {
        return qty_orgin_logi;
    }

    public void setQty_orgin_logi(int qty_orgin_logi) {
        this.qty_orgin_logi = qty_orgin_logi;
    }
}
