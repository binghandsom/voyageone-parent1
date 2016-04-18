package com.voyageone.common.configs.beans;

import java.io.Serializable;

/**
 * @description
 * @author: holysky
 * @date: 2016/4/15 18:43
 * COPYRIGHT © 2001 - 2016 VOYAGE ONE GROUP INC. ALL RIGHTS RESERVED.
 */
public class PlatformBean implements Serializable{
    int platform_id;
    String platform;
    String comment;

    public int getPlatform_id() {
        return platform_id;
    }

    public void setPlatform_id(int platform_id) {
        this.platform_id = platform_id;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
