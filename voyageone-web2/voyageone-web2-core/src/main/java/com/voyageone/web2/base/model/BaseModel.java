package com.voyageone.web2.base.model;

import com.voyageone.common.util.StringUtils;

/**
 * for all model.
 *
 * @author Edward
 * @version 2.0.0, 15/12/2
 */
public abstract class BaseModel {

    private String created;

    private String creater;

    private String modifier;

    private String modified;

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = StringUtils.isEmpty(created) ? "" : created.substring(0, 19);
    }

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater;
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
        this.modified = StringUtils.isEmpty(modified) ? "" : modified.substring(0, 19);
    }

}
