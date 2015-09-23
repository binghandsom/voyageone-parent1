package com.voyageone.batch.ims.modelbean;

/**
 * Created by Leo on 15-9-14.
 */
public class DarwinPropValue {
    private int cart_id;
    private String style_code;
    private String platform_prop_id;
    private String platform_prop_value;
    private String modifier;
    private String modified;
    private String created;
    private String creater;

    public int getCart_id() {
        return cart_id;
    }

    public void setCart_id(int cart_id) {
        this.cart_id = cart_id;
    }

    public String getStyle_code() {
        return style_code;
    }

    public void setStyle_code(String style_code) {
        this.style_code = style_code;
    }

    public String getPlatform_prop_id() {
        return platform_prop_id;
    }

    public void setPlatform_prop_id(String platform_prop_id) {
        this.platform_prop_id = platform_prop_id;
    }

    public String getPlatform_prop_value() {
        return platform_prop_value;
    }

    public void setPlatform_prop_value(String platform_prop_value) {
        this.platform_prop_value = platform_prop_value;
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
}
