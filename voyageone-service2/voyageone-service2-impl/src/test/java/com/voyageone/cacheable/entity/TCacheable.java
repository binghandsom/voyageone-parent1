package com.voyageone.cacheable.entity;

import com.voyageone.common.util.JacksonUtil;

import java.util.UUID;

/**
 * @author aooer 2016/5/5.
 * @version 2.0.0
 * @since 2.0.0
 */
public class TCacheable {

    private UUID id;

    private String name;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString(){
        return JacksonUtil.bean2Json(this);
    }
}