package com.voyageone.components.jumei.bean;

import java.util.List;

/**
 * Created by sn3 on 2015-07-18.
 */
public class JmOrderInfo extends JmBaseBean {
    private String id;
    private String order_sn;
    private String order_id;
    private String quantity;
    private String total_price;
    private String uid;
    private String timestamp;
    private String status;
    private String delivery_fee;
    private String creation_time;
    private String payment_time;
    private String delivery_time;
    private String completed_time;
    private String payment_method;
    private String trade_no;
    private String payment_amount;
    private String logistic_preference;
    private String target_shipping_time;
    private String price_discount_amount;
    private String price_discount_ratio;
    private String promo_cards;
    private String order_status;
    private String payment_status;
    private String confirm_type;
    private String balance_paid_amount;
    private String shipping_system_id;
    private String shipping_system_type;
    private String prefer_delivery_time_note;
    private String cart_key;
    private String logistic_id;
    private String logistic_track_no;
    private String shipping_status;
    private String sync_version;
    private String promo_card_discount_price;
    private String order_discount_price;
    private String shipping_load_confirm_time;
    private String user_privilege_group;
    private String order_ip;
    private String order_site;
    private String red_envelope_card_no;
    private String red_envelope_discount_price;
    private String red_envelope_discount_price_real;
    private String updated_at;
    private String is_deleted;
    private String order_type;
    private String ext_info;
    private String referer_site;
    private String attribute_selections;
    private String notify_mobile;
    private String deposit;
    private String deposit_payment_time;
    private String deposit_payment_method;
    private String deposit_payment_amount;
    private String deposit_balance_paid_amount;
    private String balance_due;
    private String balance_due_payment_time;
    private String balance_due_payment_method;
    private String balance_due_payment_amount;
    private String balance_due_balance_paid_amount;
    private String created_time;
    private String invoice_header;
    private String invoice_medium;
    private String invoice_contents;
    private String need_invoice;

    private List<Product_Infos> product_infos;
    private Receiver_Infos receiver_infos;
    private String total_products_price;
    private List<Refund_Info> refund_info;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrder_sn() {
		return order_sn;
	}

	public void setOrder_sn(String order_sn) {
		this.order_sn = order_sn;
	}

	public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getTotal_price() {
        return total_price;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDelivery_fee() {
        return delivery_fee;
    }

    public void setDelivery_fee(String delivery_fee) {
        this.delivery_fee = delivery_fee;
    }

    public String getCreation_time() {
        return creation_time;
    }

    public void setCreation_time(String creation_time) {
        this.creation_time = creation_time;
    }

    public String getPayment_time() {
        return payment_time;
    }

    public void setPayment_time(String payment_time) {
        this.payment_time = payment_time;
    }

    public String getDelivery_time() {
        return delivery_time;
    }

    public void setDelivery_time(String delivery_time) {
        this.delivery_time = delivery_time;
    }

    public String getCompleted_time() {
        return completed_time;
    }

    public void setCompleted_time(String completed_time) {
        this.completed_time = completed_time;
    }

    public String getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(String payment_method) {
        this.payment_method = payment_method;
    }

    public String getTrade_no() {
        return trade_no;
    }

    public void setTrade_no(String trade_no) {
        this.trade_no = trade_no;
    }

    public String getPayment_amount() {
        return payment_amount;
    }

    public void setPayment_amount(String payment_amount) {
        this.payment_amount = payment_amount;
    }

    public String getLogistic_preference() {
        return logistic_preference;
    }

    public void setLogistic_preference(String logistic_preference) {
        this.logistic_preference = logistic_preference;
    }

    public String getTarget_shipping_time() {
        return target_shipping_time;
    }

    public void setTarget_shipping_time(String target_shipping_time) {
        this.target_shipping_time = target_shipping_time;
    }

    public String getPrice_discount_amount() {
        return price_discount_amount;
    }

    public void setPrice_discount_amount(String price_discount_amount) {
        this.price_discount_amount = price_discount_amount;
    }

    public String getPrice_discount_ratio() {
        return price_discount_ratio;
    }

    public void setPrice_discount_ratio(String price_discount_ratio) {
        this.price_discount_ratio = price_discount_ratio;
    }

    public String getPromo_cards() {
        return promo_cards;
    }

    public void setPromo_cards(String promo_cards) {
        this.promo_cards = promo_cards;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public String getPayment_status() {
        return payment_status;
    }

    public void setPayment_status(String payment_status) {
        this.payment_status = payment_status;
    }

    public String getConfirm_type() {
        return confirm_type;
    }

    public void setConfirm_type(String confirm_type) {
        this.confirm_type = confirm_type;
    }

    public String getBalance_paid_amount() {
        return balance_paid_amount;
    }

    public void setBalance_paid_amount(String balance_paid_amount) {
        this.balance_paid_amount = balance_paid_amount;
    }

    public String getShipping_system_id() {
        return shipping_system_id;
    }

    public void setShipping_system_id(String shipping_system_id) {
        this.shipping_system_id = shipping_system_id;
    }

    public String getShipping_system_type() {
        return shipping_system_type;
    }

    public void setShipping_system_type(String shipping_system_type) {
        this.shipping_system_type = shipping_system_type;
    }

    public String getPrefer_delivery_time_note() {
        return prefer_delivery_time_note;
    }

    public void setPrefer_delivery_time_note(String prefer_delivery_time_note) {
        this.prefer_delivery_time_note = prefer_delivery_time_note;
    }

    public String getCart_key() {
        return cart_key;
    }

    public void setCart_key(String cart_key) {
        this.cart_key = cart_key;
    }

    public String getLogistic_id() {
        return logistic_id;
    }

    public void setLogistic_id(String logistic_id) {
        this.logistic_id = logistic_id;
    }

    public String getLogistic_track_no() {
        return logistic_track_no;
    }

    public void setLogistic_track_no(String logistic_track_no) {
        this.logistic_track_no = logistic_track_no;
    }

    public String getShipping_status() {
        return shipping_status;
    }

    public void setShipping_status(String shipping_status) {
        this.shipping_status = shipping_status;
    }

    public String getSync_version() {
        return sync_version;
    }

    public void setSync_version(String sync_version) {
        this.sync_version = sync_version;
    }

    public String getPromo_card_discount_price() {
        return promo_card_discount_price;
    }

    public void setPromo_card_discount_price(String promo_card_discount_price) {
        this.promo_card_discount_price = promo_card_discount_price;
    }

    public String getOrder_discount_price() {
        return order_discount_price;
    }

    public void setOrder_discount_price(String order_discount_price) {
        this.order_discount_price = order_discount_price;
    }

    public String getShipping_load_confirm_time() {
        return shipping_load_confirm_time;
    }

    public void setShipping_load_confirm_time(String shipping_load_confirm_time) {
        this.shipping_load_confirm_time = shipping_load_confirm_time;
    }

    public String getUser_privilege_group() {
        return user_privilege_group;
    }

    public void setUser_privilege_group(String user_privilege_group) {
        this.user_privilege_group = user_privilege_group;
    }

    public String getOrder_ip() {
        return order_ip;
    }

    public void setOrder_ip(String order_ip) {
        this.order_ip = order_ip;
    }

    public String getOrder_site() {
        return order_site;
    }

    public void setOrder_site(String order_site) {
        this.order_site = order_site;
    }

    public String getRed_envelope_card_no() {
        return red_envelope_card_no;
    }

    public void setRed_envelope_card_no(String red_envelope_card_no) {
        this.red_envelope_card_no = red_envelope_card_no;
    }

    public String getRed_envelope_discount_price() {
        return red_envelope_discount_price;
    }

    public void setRed_envelope_discount_price(String red_envelope_discount_price) {
        this.red_envelope_discount_price = red_envelope_discount_price;
    }

    public String getRed_envelope_discount_price_real() {
        return red_envelope_discount_price_real;
    }

    public void setRed_envelope_discount_price_real(String red_envelope_discount_price_real) {
        this.red_envelope_discount_price_real = red_envelope_discount_price_real;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getIs_deleted() {
        return is_deleted;
    }

    public void setIs_deleted(String is_deleted) {
        this.is_deleted = is_deleted;
    }

    public String getOrder_type() {
        return order_type;
    }

    public void setOrder_type(String order_type) {
        this.order_type = order_type;
    }

    public String getExt_info() {
        return ext_info;
    }

    public void setExt_info(String ext_info) {
        this.ext_info = ext_info;
    }

    public String getReferer_site() {
        return referer_site;
    }

    public void setReferer_site(String referer_site) {
        this.referer_site = referer_site;
    }

    public String getAttribute_selections() {
        return attribute_selections;
    }

    public void setAttribute_selections(String attribute_selections) {
        this.attribute_selections = attribute_selections;
    }

    public String getNotify_mobile() {
        return notify_mobile;
    }

    public void setNotify_mobile(String notify_mobile) {
        this.notify_mobile = notify_mobile;
    }

    public String getDeposit() {
        return deposit;
    }

    public void setDeposit(String deposit) {
        this.deposit = deposit;
    }

    public String getDeposit_payment_time() {
        return deposit_payment_time;
    }

    public void setDeposit_payment_time(String deposit_payment_time) {
        this.deposit_payment_time = deposit_payment_time;
    }

    public String getDeposit_payment_method() {
        return deposit_payment_method;
    }

    public void setDeposit_payment_method(String deposit_payment_method) {
        this.deposit_payment_method = deposit_payment_method;
    }

    public String getDeposit_payment_amount() {
        return deposit_payment_amount;
    }

    public void setDeposit_payment_amount(String deposit_payment_amount) {
        this.deposit_payment_amount = deposit_payment_amount;
    }

    public String getDeposit_balance_paid_amount() {
        return deposit_balance_paid_amount;
    }

    public void setDeposit_balance_paid_amount(String deposit_balance_paid_amount) {
        this.deposit_balance_paid_amount = deposit_balance_paid_amount;
    }

    public String getBalance_due() {
        return balance_due;
    }

    public void setBalance_due(String balance_due) {
        this.balance_due = balance_due;
    }

    public String getBalance_due_payment_time() {
        return balance_due_payment_time;
    }

    public void setBalance_due_payment_time(String balance_due_payment_time) {
        this.balance_due_payment_time = balance_due_payment_time;
    }

    public String getBalance_due_payment_method() {
        return balance_due_payment_method;
    }

    public void setBalance_due_payment_method(String balance_due_payment_method) {
        this.balance_due_payment_method = balance_due_payment_method;
    }

    public String getBalance_due_payment_amount() {
        return balance_due_payment_amount;
    }

    public void setBalance_due_payment_amount(String balance_due_payment_amount) {
        this.balance_due_payment_amount = balance_due_payment_amount;
    }

    public String getBalance_due_balance_paid_amount() {
        return balance_due_balance_paid_amount;
    }

    public void setBalance_due_balance_paid_amount(String balance_due_balance_paid_amount) {
        this.balance_due_balance_paid_amount = balance_due_balance_paid_amount;
    }

    public String getCreated_time() {
        return created_time;
    }

    public void setCreated_time(String created_time) {
        this.created_time = created_time;
    }

    public String getInvoice_header() {
        return invoice_header;
    }

    public void setInvoice_header(String invoice_header) {
        this.invoice_header = invoice_header;
    }

    public String getInvoice_medium() {
        return invoice_medium;
    }

    public void setInvoice_medium(String invoice_medium) {
        this.invoice_medium = invoice_medium;
    }

    public String getInvoice_contents() {
        return invoice_contents;
    }

    public void setInvoice_contents(String invoice_contents) {
        this.invoice_contents = invoice_contents;
    }

    public String getNeed_invoice() {
        return need_invoice;
    }

    public void setNeed_invoice(String need_invoice) {
        this.need_invoice = need_invoice;
    }

    public List<Product_Infos> getProduct_infos() {
        return product_infos;
    }

    public void setProduct_infos(List<Product_Infos> product_infos) {
        this.product_infos = product_infos;
    }

    public Receiver_Infos getReceiver_infos() {
        return receiver_infos;
    }

    public void setReceiver_infos(Receiver_Infos receiver_infos) {
        this.receiver_infos = receiver_infos;
    }

    public String getTotal_products_price() {
        return total_products_price;
    }

    public void setTotal_products_price(String total_products_price) {
        this.total_products_price = total_products_price;
    }

    public List<Refund_Info> getRefund_info() {
        return refund_info;
    }

    public void setRefund_info(List<Refund_Info> refund_info) {
        this.refund_info = refund_info;
    }
}
