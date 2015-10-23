package com.voyageone.batch.wms.modelbean;

/**
 * @author jack
 * bulkShipment头记录Bean
 */
public class bulkShipmentHeadBean {

    private String header_identifier;
    private String hdl_unit_exid;
    private String kuwev_kunnr;
    private String likp_bldat;
    private String total_quantity;
    private String ship_via_code;
    private String tracking_num;
    private String shipping_material;
    private String cancel_flag;

    public String getHeader_identifier() {
        return header_identifier;
    }

    public void setHeader_identifier(String header_identifier) {
        this.header_identifier = header_identifier;
    }

    public String getHdl_unit_exid() {
        return hdl_unit_exid;
    }

    public void setHdl_unit_exid(String hdl_unit_exid) {
        this.hdl_unit_exid = hdl_unit_exid;
    }

    public String getKuwev_kunnr() {
        return kuwev_kunnr;
    }

    public void setKuwev_kunnr(String kuwev_kunnr) {
        this.kuwev_kunnr = kuwev_kunnr;
    }

    public String getLikp_bldat() {
        return likp_bldat;
    }

    public void setLikp_bldat(String likp_bldat) {
        this.likp_bldat = likp_bldat;
    }

    public String getTotal_quantity() {
        return total_quantity;
    }

    public void setTotal_quantity(String total_quantity) {
        this.total_quantity = total_quantity;
    }

    public String getShip_via_code() {
        return ship_via_code;
    }

    public void setShip_via_code(String ship_via_code) {
        this.ship_via_code = ship_via_code;
    }

    public String getTracking_num() {
        return tracking_num;
    }

    public void setTracking_num(String tracking_num) {
        this.tracking_num = tracking_num;
    }

    public String getShipping_material() {
        return shipping_material;
    }

    public void setShipping_material(String shipping_material) {
        this.shipping_material = shipping_material;
    }

    public String getCancel_flag() {
        return cancel_flag;
    }

    public void setCancel_flag(String cancel_flag) {
        this.cancel_flag = cancel_flag;
    }
}
