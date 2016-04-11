package com.voyageone.components.gilt.bean;

/**
 * @deprecated
 * @author aooer 2016/2/1.
 * @version 2.0.0
 * @since 2.0.0
 */
public class GiltAttribute {

    private GiltColor color;

    private String style;

    private GiltSize size;

    private String material;

    public GiltColor getColor() {
        return color;
    }

    public void setColor(GiltColor color) {
        this.color = color;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public GiltSize getSize() {
        return size;
    }

    public void setSize(GiltSize size) {
        this.size = size;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }
}
