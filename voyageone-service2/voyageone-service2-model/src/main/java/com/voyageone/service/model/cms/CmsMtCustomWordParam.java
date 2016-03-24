package com.voyageone.service.model.cms;

/**
 * 对表 voyageone_ims.ims_mt_custom_word_param 的映射
 *
 * Created by Jonas on 9/11/15.
 */
public class CmsMtCustomWordParam {

    private int param_id;

    private String param_name;

    private String param_desc;

    private int word_id;

    private String created;

    private String creater;

    private String modified;

    private String modifier;

    public int getParam_id() {
        return param_id;
    }

    public void setParam_id(int param_id) {
        this.param_id = param_id;
    }

    public String getParam_name() {
        return param_name;
    }

    public void setParam_name(String param_name) {
        this.param_name = param_name;
    }

    public String getParam_desc() {
        return param_desc;
    }

    public void setParam_desc(String param_desc) {
        this.param_desc = param_desc;
    }

    public int getWord_id() {
        return word_id;
    }

    public void setWord_id(int word_id) {
        this.word_id = word_id;
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
