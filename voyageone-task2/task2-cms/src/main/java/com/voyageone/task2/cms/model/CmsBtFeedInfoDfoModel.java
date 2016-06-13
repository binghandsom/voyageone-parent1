package com.voyageone.task2.cms.model;

import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel;

/**
 * @author james.li on 2016/1/15.
 * @version 2.0.0
 */
public class CmsBtFeedInfoDfoModel extends CmsBtFeedInfoModel {

    private String lensSize;
    private String bridge_size;
    private String templeSize;
    private String verticalSize;
    private String lens_color;
    private String lensTechnology;
    private String frameType;
    private String style;

    public String getLensSize() {
        return lensSize;
    }

    public void setLensSize(String lensSize) {
        this.lensSize = lensSize;
    }

    public String getBridge_size() {
        return bridge_size;
    }

    public void setBridge_size(String bridge_size) {
        this.bridge_size = bridge_size;
    }

    public String getTempleSize() {
        return templeSize;
    }

    public void setTempleSize(String templeSize) {
        this.templeSize = templeSize;
    }

    public String getVerticalSize() {
        return verticalSize;
    }

    public void setVerticalSize(String verticalSize) {
        this.verticalSize = verticalSize;
    }

    public String getLens_color() {
        return lens_color;
    }

    public void setLens_color(String lens_color) {
        this.lens_color = lens_color;
    }

    public String getLensTechnology() {
        return lensTechnology;
    }

    public void setLensTechnology(String lensTechnology) {
        this.lensTechnology = lensTechnology;
    }

    public String getFrameType() {
        return frameType;
    }

    public void setFrameType(String frameType) {
        this.frameType = frameType;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }
}
