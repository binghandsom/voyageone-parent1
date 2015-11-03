package com.voyageone.batch.wms.modelbean;

/**
 * @author jack
 * ClientPackageItemBean
 */
public class ClientPackageItemBean {

    private long package_item_id;
    private long shipment_id;
    private long package_id;
    private String carton_line_number;
    private String sto_no;
    private String sto_line_no;
    private String article_number;
    private String shipped_qty;
    private int calc_qty;
    private String fact_unit_nom;
    private String base_uom;
    private String stock_type;
    private String site;
    private String shipping_point;
    private String upc;
    private String active;
    private String created;
    private String creater;
    private String modified;
    private String modifier;

    public long getPackage_item_id() {
        return package_item_id;
    }

    public void setPackage_item_id(long package_item_id) {
        this.package_item_id = package_item_id;
    }

    public long getShipment_id() {
        return shipment_id;
    }

    public void setShipment_id(long shipment_id) {
        this.shipment_id = shipment_id;
    }

    public long getPackage_id() {
        return package_id;
    }

    public void setPackage_id(long package_id) {
        this.package_id = package_id;
    }

    public String getCarton_line_number() {
        return carton_line_number;
    }

    public void setCarton_line_number(String carton_line_number) {
        this.carton_line_number = carton_line_number;
    }

    public String getSto_no() {
        return sto_no;
    }

    public void setSto_no(String sto_no) {
        this.sto_no = sto_no;
    }

    public String getSto_line_no() {
        return sto_line_no;
    }

    public void setSto_line_no(String sto_line_no) {
        this.sto_line_no = sto_line_no;
    }

    public String getArticle_number() {
        return article_number;
    }

    public void setArticle_number(String article_number) {
        this.article_number = article_number;
    }

    public String getShipped_qty() {
        return shipped_qty;
    }

    public void setShipped_qty(String shipped_qty) {
        this.shipped_qty = shipped_qty;
    }

    public int getCalc_qty() {
        return calc_qty;
    }

    public void setCalc_qty(int calc_qty) {
        this.calc_qty = calc_qty;
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

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getShipping_point() {
        return shipping_point;
    }

    public void setShipping_point(String shipping_point) {
        this.shipping_point = shipping_point;
    }

    public String getUpc() {
        return upc;
    }

    public void setUpc(String upc) {
        this.upc = upc;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
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
