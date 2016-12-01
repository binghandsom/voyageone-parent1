package com.voyageone.service.model.user;


import java.io.Serializable;
import java.util.Date;

/**
 * @author Ethan Shi
 *
 */

public class CoreBaseModel  implements Serializable {

    protected Integer id;

    protected Date created;

    protected String creater;

    protected Date modified;

    protected String modifier;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater;
    }

    public Date getModified() {
        return modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

}
