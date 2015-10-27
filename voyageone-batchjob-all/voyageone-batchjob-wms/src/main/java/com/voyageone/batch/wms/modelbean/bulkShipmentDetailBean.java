package com.voyageone.batch.wms.modelbean;

/**
 * @author jack
 * bulkShipment明细记录Bean
 */
public class bulkShipmentDetailBean {

    private String item_identifier;
    private String carton_line;
    private String lips_vgbel;
    private String lips_posnr;
    private String lips_matnr;
    private String dlv_qty;
    private String fact_unit_nom;
    private String base_uom;
    private String stock_type;
    private String plant;
    private String stge_loc;
    private String upc;

    public String getItem_identifier() {
        return item_identifier;
    }

    public void setItem_identifier(String item_identifier) {
        this.item_identifier = item_identifier;
    }

    public String getCarton_line() {
        return carton_line;
    }

    public void setCarton_line(String carton_line) {
        this.carton_line = carton_line;
    }

    public String getLips_vgbel() {
        return lips_vgbel;
    }

    public void setLips_vgbel(String lips_vgbel) {
        this.lips_vgbel = lips_vgbel;
    }

    public String getLips_posnr() {
        return lips_posnr;
    }

    public void setLips_posnr(String lips_posnr) {
        this.lips_posnr = lips_posnr;
    }

    public String getLips_matnr() {
        return lips_matnr;
    }

    public void setLips_matnr(String lips_matnr) {
        this.lips_matnr = lips_matnr;
    }

    public String getDlv_qty() {
        return dlv_qty;
    }

    public void setDlv_qty(String dlv_qty) {
        this.dlv_qty = dlv_qty;
    }

    public String getFact_unit_nom() {
        return fact_unit_nom;
    }

    public void setFact_unit_nom(String fact_unit_nom) {
        this.fact_unit_nom = fact_unit_nom;
    }

    public String getBase_uom() {
        return base_uom;
    }

    public void setBase_uom(String base_uom) {
        this.base_uom = base_uom;
    }

    public String getStock_type() {
        return stock_type;
    }

    public void setStock_type(String stock_type) {
        this.stock_type = stock_type;
    }

    public String getPlant() {
        return plant;
    }

    public void setPlant(String plant) {
        this.plant = plant;
    }

    public String getStge_loc() {
        return stge_loc;
    }

    public void setStge_loc(String stge_loc) {
        this.stge_loc = stge_loc;
    }

    public String getUpc() {
        return upc;
    }

    public void setUpc(String upc) {
        this.upc = upc;
    }
}
