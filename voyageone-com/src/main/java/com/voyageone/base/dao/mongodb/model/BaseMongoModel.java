package com.voyageone.base.dao.mongodb.model;

import org.springframework.data.annotation.Id;

public class BaseMongoModel {
    @Id
    protected String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
