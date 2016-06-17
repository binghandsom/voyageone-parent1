package com.voyageone.common.configs.beans;
/**
 * Created by Jack on 4/21/2015.
 */
public class TypeChannelBean {

    private int id;
    private int type_id;
    private String channel_id;
    private String type_code;
    private String value;
    private String name;
    private String add_name1;
    private String add_name2;
    private String lang_id;
    private int display_order;
    private String comment;
    private String cart_type;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getChannel_id() {
        return channel_id;
    }

    public void setChannel_id(String channel_id) {
        this.channel_id = channel_id;
    }

    public int getType_id() {
        return type_id;
    }

    public void setType_id(int type_id) {
        this.type_id = type_id;
    }

    public String getType_code() {
        return type_code;
    }

    public void setType_code(String type_code) {
        this.type_code = type_code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAdd_name1() {
        return add_name1;
    }

    public void setAdd_name1(String add_name1) {
        this.add_name1 = add_name1;
    }

    public String getAdd_name2() {
        return add_name2;
    }

    public void setAdd_name2(String add_name2) {
        this.add_name2 = add_name2;
    }

    public String getLang_id() {
        return lang_id;
    }

    public void setLang_id(String lang_id) {
        this.lang_id = lang_id;
    }

    public int getDisplay_order() {
        return display_order;
    }

    public void setDisplay_order(int display_order) {
        this.display_order = display_order;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCartType() {
        return cart_type;
    }

    public void setCartType(String cart_type) {
        this.cart_type = cart_type;
    }
}
