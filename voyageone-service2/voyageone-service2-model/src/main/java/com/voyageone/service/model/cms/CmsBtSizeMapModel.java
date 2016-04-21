package com.voyageone.service.model.cms;

import com.voyageone.base.dao.mysql.BaseModel;

/**
 * Created by morse.lu on 16/4/20.
 */
public class CmsBtSizeMapModel extends BaseModel {
    private int id;
    private String sizeMapGroupId;
    private String originalSize;
    private String adjustSize;
    private int active;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSizeMapGroupId() {
        return sizeMapGroupId;
    }

    public void setSizeMapGroupId(String sizeMapGroupId) {
        this.sizeMapGroupId = sizeMapGroupId;
    }

    public String getOriginalSize() {
        return originalSize;
    }

    public void setOriginalSize(String originalSize) {
        this.originalSize = originalSize;
    }

    public String getAdjustSize() {
        return adjustSize;
    }

    public void setAdjustSize(String adjustSize) {
        this.adjustSize = adjustSize;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }
}
