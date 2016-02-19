package com.voyageone.batch.synship.modelbean;

/**
 * Created by Jonas on 9/22/15.
 */
public class ShortUrlBean {
    /**
     * ID
     */
    private String id ="";

    /**
     * 短链接key
     */
    private String short_key ="";

    /**
     * 订单ID
     */
    private String source_order_id ="";

    /**
     * 注释
     */
    private String comment ="";

    /**
     * 收货人电话
     */
    private String ship_phone ="";

    /**
     * 收货人姓名
     */
    private String ship_name ="";

    /**
     * 类型
     */
    private String type ="";

    /**
     * 短信标志位
     */
    private String sms_flg ="";

    /**
     * 创建时间
     */
    private String create_time ="";

    /**
     * 更新时间
     */
    private String update_time ="";

    /**
     * 创建者
     */
    private String create_person ="";

    /**
     * 更新者
     */
    private String update_person ="";

    /**
     * 身份证上传标志位
     */
    private String upload_flg ="";

    /**
     * 张峰   重发身份证上传短信  2015年04月02日
     * 身份证上传消息发送次数
     */
    private String sms_count = "";

    /**
     * 张峰   老客户重复购买判断追加   2015年04月08日
     * 购买人昵称
     */
    protected String buyer_nick;

    /**
     * 张峰   老客户重复购买判断追加   2015年04月08日
     * 销售渠道
     */
    protected String order_channel_id;

    /**
     * 张峰   老客户重复购买判断追加   2015年04月08日
     * 旺旺ID+Ship_nick
     */
    protected String orders_name;

    protected String cart_id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getShort_key() {
        return short_key;
    }

    public void setShort_key(String short_key) {
        this.short_key = short_key;
    }

    public String getSource_order_id() {
        return source_order_id;
    }

    public void setSource_order_id(String source_order_id) {
        this.source_order_id = source_order_id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getShip_phone() {
        return ship_phone;
    }

    public void setShip_phone(String ship_phone) {
        this.ship_phone = ship_phone;
    }

    public String getShip_name() {
        return ship_name;
    }

    public void setShip_name(String ship_name) {
        this.ship_name = ship_name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSms_flg() {
        return sms_flg;
    }

    public void setSms_flg(String sms_flg) {
        this.sms_flg = sms_flg;
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

    public String getUpload_flg() {
        return upload_flg;
    }

    public void setUpload_flg(String upload_flg) {
        this.upload_flg = upload_flg;
    }

    public String getSms_count() {
        return sms_count;
    }

    public void setSms_count(String sms_count) {
        this.sms_count = sms_count;
    }

    public String getBuyer_nick() {
        return buyer_nick;
    }

    public void setBuyer_nick(String buyer_nick) {
        this.buyer_nick = buyer_nick;
    }

    public String getOrder_channel_id() {
        return order_channel_id;
    }

    public void setOrder_channel_id(String order_channel_id) {
        this.order_channel_id = order_channel_id;
    }

    public String getOrders_name() {
        return orders_name;
    }

    public void setOrders_name(String orders_name) {
        this.orders_name = orders_name;
    }

    public String getCart_id() {
        return cart_id;
    }

    public void setCart_id(String cart_id) {
        this.cart_id = cart_id;
    }
}
