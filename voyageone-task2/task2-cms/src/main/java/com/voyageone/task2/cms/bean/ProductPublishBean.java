package com.voyageone.task2.cms.bean;

/**
 * Created by Leo on 15-7-24.
 */
public class ProductPublishBean {
    private int product_id;
    private String channel_id;
    private int cart_id;
    private int model_id;
    private String code;
    private int is_published;
    private int cn_group_id;
    private String publish_product_id;
    private int publish_product_status;
    private int main_product_flg;
    private String quantity_update_type;
    private int publish_status;
    private String num_iid;
    private String publish_datetime;
    private String publish_faild_comment;
    private String creater;
    private String created;
    private String modifier;
    private String modified;

    public ProductPublishBean() {
        main_product_flg = -1;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public String getChannel_id() {
        return channel_id;
    }

    public void setChannel_id(String channel_id) {
        this.channel_id = channel_id;
    }

    public int getCart_id() {
        return cart_id;
    }

    public void setCart_id(int cart_id) {
        this.cart_id = cart_id;
    }

    public int getModel_id() {
        return model_id;
    }

    public void setModel_id(int model_id) {
        this.model_id = model_id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getIs_published() {
        return is_published;
    }

    public void setIs_published(int is_published) {
        this.is_published = is_published;
    }

    public int getCn_group_id() {
        return cn_group_id;
    }

    public void setCn_group_id(int cn_group_id) {
        this.cn_group_id = cn_group_id;
    }

    public String getPublish_product_id() {
        return publish_product_id;
    }

    public void setPublish_product_id(String publish_product_id) {
        this.publish_product_id = publish_product_id;
    }

    public int getPublish_product_status() {
        return publish_product_status;
    }

    public void setPublish_product_status(int publish_product_status) {
        this.publish_product_status = publish_product_status;
    }

    public int getMain_product_flg() {
        return main_product_flg;
    }

    public void setMain_product_flg(int main_product_flg) {
        this.main_product_flg = main_product_flg;
    }

    public String getQuantity_update_type() {
        return quantity_update_type;
    }

    public void setQuantity_update_type(String quantity_update_type) {
        this.quantity_update_type = quantity_update_type;
    }

    public int getPublish_status() {
        return publish_status;
    }

    public void setPublish_status(int publish_status) {
        this.publish_status = publish_status;
    }

    public String getNum_iid() {
        return num_iid;
    }

    public void setNum_iid(String num_iid) {
        this.num_iid = num_iid;
    }

    public String getPublish_datetime() {
        return publish_datetime;
    }

    public void setPublish_datetime(String publish_datetime) {
        this.publish_datetime = publish_datetime;
    }

    public String getPublish_faild_comment() {
        return publish_faild_comment;
    }

    public void setPublish_faild_comment(String publish_faild_comment) {
        this.publish_faild_comment = publish_faild_comment;
    }

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }
}
