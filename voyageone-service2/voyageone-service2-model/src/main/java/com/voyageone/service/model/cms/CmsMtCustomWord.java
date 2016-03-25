package com.voyageone.service.model.cms;

import com.voyageone.base.dao.mysql.BaseModel;

import java.util.List;

/**
 * 对表 voyageone_ims.ims_mt_custom_word 的映射
 *
 * Created by Jonas on 9/11/15.
 */
public class CmsMtCustomWord extends BaseModel {
    private int word_id;

    private String word_name;

    private String word_desc;

    private String created;

    private String creater;

    private String modified;

    private String modifier;

    private List<CmsMtCustomWordParam> params;

    public int getWord_id() {
        return word_id;
    }

    public void setWord_id(int word_id) {
        this.word_id = word_id;
    }

    public String getWord_name() {
        return word_name;
    }

    public void setWord_name(String word_name) {
        this.word_name = word_name;
    }

    public String getWord_desc() {
        return word_desc;
    }

    public void setWord_desc(String word_desc) {
        this.word_desc = word_desc;
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

    public List<CmsMtCustomWordParam> getParams() {
        return params;
    }

    public void setParams(List<CmsMtCustomWordParam> params) {
        this.params = params;
    }
}
