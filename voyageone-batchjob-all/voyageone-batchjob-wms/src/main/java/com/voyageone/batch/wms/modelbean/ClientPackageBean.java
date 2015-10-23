package com.voyageone.batch.wms.modelbean;

/**
 * @author jack
 * ClientPackageBean
 */
public class ClientPackageBean {

    private long package_id;
    private long shipment_id;
    private String ucc128_carton_no;
    private String ship_to_store;
    private String ship_date;
    private String total_carton_quantity;
    private String ship_via;
    private String tracking_no;
    private String shipping_material;
    private String cancellation_flag;
    private String active;
    private String created;
    private String creater;
    private String modified;
    private String modifier;

    public long getPackage_id() {
        return package_id;
    }

    public void setPackage_id(long package_id) {
        this.package_id = package_id;
    }

    public long getShipment_id() {
        return shipment_id;
    }

    public void setShipment_id(long shipment_id) {
        this.shipment_id = shipment_id;
    }

    public String getUcc128_carton_no() {
        return ucc128_carton_no;
    }

    public void setUcc128_carton_no(String ucc128_carton_no) {
        this.ucc128_carton_no = ucc128_carton_no;
    }

    public String getShip_to_store() {
        return ship_to_store;
    }

    public void setShip_to_store(String ship_to_store) {
        this.ship_to_store = ship_to_store;
    }

    public String getShip_date() {
        return ship_date;
    }

    public void setShip_date(String ship_date) {
        this.ship_date = ship_date;
    }

    public String getTotal_carton_quantity() {
        return total_carton_quantity;
    }

    public void setTotal_carton_quantity(String total_carton_quantity) {
        this.total_carton_quantity = total_carton_quantity;
    }

    public String getShip_via() {
        return ship_via;
    }

    public void setShip_via(String ship_via) {
        this.ship_via = ship_via;
    }

    public String getTracking_no() {
        return tracking_no;
    }

    public void setTracking_no(String tracking_no) {
        this.tracking_no = tracking_no;
    }

    public String getShipping_material() {
        return shipping_material;
    }

    public void setShipping_material(String shipping_material) {
        this.shipping_material = shipping_material;
    }

    public String getCancellation_flag() {
        return cancellation_flag;
    }

    public void setCancellation_flag(String cancellation_flag) {
        this.cancellation_flag = cancellation_flag;
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
