package com.voyageone.components.jumei.Bean;


import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;

/**
 * Base Data Structure.
 *
 * Created on 2015-12-14
 *
 * @author chuanyu.liang
 * @version 2.0.0
 * @since 2.0.0
 */
public abstract class JmBaseBean implements Serializable {

    @JsonIgnore
    protected String created;
    @JsonIgnore
    protected String creater;
    @JsonIgnore
    protected String modified;
    @JsonIgnore
    protected String modifier;

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

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }


}
