package com.voyageone.batch.wms.modelbean;

/**
 * Created by Tester on 5/21/2015.
 *
 * @author Jack
 */
public class ReservationBean {
    private long id;
    private String order_channel_id;
    private long order_number;
    private String sku;
    private String product;
    private long store_id;
    private String store;
    private String status;
    private String reference;
    private String upload_time;
    private String date;
    private String printed;
    private String note;
    private String cart_id;
    private String srcCart_id;
    private String source_order_id;
    private String pos_processed;
    private String syn_ship_no;
    private String ship_channel;
    private double declare_price;
    private String description_inner;
    private String description_short;
    private String description;
    private String brand;
    private String gender;
    private String upper_material;
    private String sole_material;
    private String origin;
    private double price;
    private String price_unit;
    private double sale_price;
    private String sale_price_unit;
    private double original_price;
    private String original_price_unit;
    private String weight_kg;
    private String weight_lb;
    private String unit;
    private String hs_code;
    private String hs_code_pu;
    private String unit_pu;
    private String hs_description_pu;
    private String hs_description;
    private String sent_flg;
    private String close_day_flg;
    private String create_time;
    private String update_time;
    private String create_person;
    private String update_person;
    private long item_number;
    private String itemCode;
    private ItemCodeBean itemCodeInfo;
    private String client_sku;
    private String sales_check_number;
    private String tracking_number;
    private String reservation_status;
    private String tracking_no;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getOrder_channel_id() {
        return order_channel_id;
    }

    public void setOrder_channel_id(String order_channel_id) {
        this.order_channel_id = order_channel_id;
    }

    public long getOrder_number() {
        return order_number;
    }

    public void setOrder_number(long order_number) {
        this.order_number = order_number;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public long getStore_id() {
        return store_id;
    }

    public void setStore_id(long store_id) {
        this.store_id = store_id;
    }

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getUpload_time() {
        return upload_time;
    }

    public void setUpload_time(String upload_time) {
        this.upload_time = upload_time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPrinted() {
        return printed;
    }

    public void setPrinted(String printed) {
        this.printed = printed;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getCart_id() {
        return cart_id;
    }

    public void setCart_id(String cart_id) {
        this.cart_id = cart_id;
    }

    public String getSrcCart_id() {
        return srcCart_id;
    }

    public void setSrcCart_id(String srcCart_id) {
        this.srcCart_id = srcCart_id;
    }

    public String getSource_order_id() {
        return source_order_id;
    }

    public void setSource_order_id(String source_order_id) {
        this.source_order_id = source_order_id;
    }

    public String getPos_processed() {
        return pos_processed;
    }

    public void setPos_processed(String pos_processed) {
        this.pos_processed = pos_processed;
    }

    public String getSyn_ship_no() {
        return syn_ship_no;
    }

    public void setSyn_ship_no(String syn_ship_no) {
        this.syn_ship_no = syn_ship_no;
    }

    public String getShip_channel() {
        return ship_channel;
    }

    public void setShip_channel(String ship_channel) {
        this.ship_channel = ship_channel;
    }

    public double getDeclare_price() {
        return declare_price;
    }

    public void setDeclare_price(double declare_price) {
        this.declare_price = declare_price;
    }

    public String getDescription_inner() {
        return description_inner;
    }

    public void setDescription_inner(String description_inner) {
        this.description_inner = description_inner;
    }

    public String getDescription_short() {
        return description_short;
    }

    public void setDescription_short(String description_short) {
        this.description_short = description_short;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getUpper_material() {
        return upper_material;
    }

    public void setUpper_material(String upper_material) {
        this.upper_material = upper_material;
    }

    public String getSole_material() {
        return sole_material;
    }

    public void setSole_material(String sole_material) {
        this.sole_material = sole_material;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getPrice_unit() {
        return price_unit;
    }

    public void setPrice_unit(String price_unit) {
        this.price_unit = price_unit;
    }

    public double getSale_price() {
        return sale_price;
    }

    public void setSale_price(double sale_price) {
        this.sale_price = sale_price;
    }

    public String getSale_price_unit() {
        return sale_price_unit;
    }

    public void setSale_price_unit(String sale_price_unit) {
        this.sale_price_unit = sale_price_unit;
    }

    public double getOriginal_price() {
        return original_price;
    }

    public void setOriginal_price(double original_price) {
        this.original_price = original_price;
    }

    public String getOriginal_price_unit() {
        return original_price_unit;
    }

    public void setOriginal_price_unit(String original_price_unit) {
        this.original_price_unit = original_price_unit;
    }

    public String getWeight_kg() {
        return weight_kg;
    }

    public void setWeight_kg(String weight_kg) {
        this.weight_kg = weight_kg;
    }

    public String getWeight_lb() {
        return weight_lb;
    }

    public void setWeight_lb(String weight_lb) {
        this.weight_lb = weight_lb;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getHs_code() {
        return hs_code;
    }

    public void setHs_code(String hs_code) {
        this.hs_code = hs_code;
    }

    public String getHs_code_pu() {
        return hs_code_pu;
    }

    public void setHs_code_pu(String hs_code_pu) {
        this.hs_code_pu = hs_code_pu;
    }

    public String getUnit_pu() {
        return unit_pu;
    }

    public void setUnit_pu(String unit_pu) {
        this.unit_pu = unit_pu;
    }

    public String getHs_description_pu() {
        return hs_description_pu;
    }

    public void setHs_description_pu(String hs_description_pu) {
        this.hs_description_pu = hs_description_pu;
    }

    public String getHs_description() {
        return hs_description;
    }

    public void setHs_description(String hs_description) {
        this.hs_description = hs_description;
    }

    public String getSent_flg() {
        return sent_flg;
    }

    public void setSent_flg(String sent_flg) {
        this.sent_flg = sent_flg;
    }

    public String getClose_day_flg() {
        return close_day_flg;
    }

    public void setClose_day_flg(String close_day_flg) {
        this.close_day_flg = close_day_flg;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public String getCreate_person() {
        return create_person;
    }

    public void setCreate_person(String create_person) {
        this.create_person = create_person;
    }

    public String getUpdate_person() {
        return update_person;
    }

    public void setUpdate_person(String update_person) {
        this.update_person = update_person;
    }

    public long getItem_number() {
        return item_number;
    }

    public void setItem_number(long item_number) {
        this.item_number = item_number;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public ItemCodeBean getItemCodeInfo() {
        return itemCodeInfo;
    }

    public void setItemCodeInfo(ItemCodeBean itemCodeInfo) {
        this.itemCodeInfo = itemCodeInfo;
    }

    public String getClient_sku() {
        return client_sku;
    }

    public void setClient_sku(String client_sku) {
        this.client_sku = client_sku;
    }

    public String getSales_check_number() {
        return sales_check_number;
    }

    public void setSales_check_number(String sales_check_number) {
        this.sales_check_number = sales_check_number;
    }

    public String getTracking_number() {
        return tracking_number;
    }

    public void setTracking_number(String tracking_number) {
        this.tracking_number = tracking_number;
    }

    public String getReservation_status() {
        return reservation_status;
    }

    public void setReservation_status(String reservation_status) {
        this.reservation_status = reservation_status;
    }

    public String getTracking_no() {
        return tracking_no;
    }

    public void setTracking_no(String tracking_no) {
        this.tracking_no = tracking_no;
    }
}
