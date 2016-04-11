package com.voyageone.components.gilt.bean;

import org.springframework.util.Assert;

import java.util.UUID;

/**
 * @author aooer 2016/2/2.
 * @version 2.0.0
 * @since 2.0.0
 */
public class GiltPatchOrderRequest {

    private UUID id;

    private GiltOrderStatus status;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public GiltOrderStatus getStatus() {
        return status;
    }

    public void setStatus(GiltOrderStatus status) {
        this.status = status;
    }

    public void check(){
        Assert.notNull(id);
        Assert.notNull(status);
    }

}
