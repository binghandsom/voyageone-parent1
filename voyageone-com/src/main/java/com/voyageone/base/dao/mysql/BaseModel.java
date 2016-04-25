package com.voyageone.base.dao.mysql;

/**
 * 对数据模型提供基础字段
 * @author Jonas
 * @version 2.0.0, 12/4/15
 */
public class BaseModel {

    private int id;

    private String created;

    private String creater;

    private String modified;

    private String modifier;

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

    /**

     */
    public int getId() {

        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
