package com.voyageone.base.dao.mongodb.test.dao;

import com.voyageone.base.dao.mongodb.model.BaseMongoModel;

import java.util.HashMap;

/**
 * Created by sn3 on 2015-10-22.
 */
public class PersonHash extends BaseMongoModel {

    private String person_name;
    private String person_id;

    private HashMap<String,String> field;

    public String getPerson_name() {
        return person_name;
    }

    public void setPerson_name(String person_name) {
        this.person_name = person_name;
    }

    public String getPerson_id() {
        return person_id;
    }

    public void setPerson_id(String person_id) {
        this.person_id = person_id;
    }

    public HashMap<String, String> getField() {
        return field;
    }

    public void setField(HashMap<String, String> field) {
        this.field = field;
    }
}
