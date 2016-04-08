package com.voyageone.components.gilt.bean;

/**
 * @author aooer 2016/2/1.
 * @version 2.0.0
 * @since 2.0.0
 */
public class GiltErrorResult {

    private String message;//	Text describing what the error is

    private Long sku_id;//	Optional Sku identifier that is related to the error

    private GiltErrorType type;//	The type of the error. This allows partners to automate error handling. See below for the available error types

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getSku_id() {
        return sku_id;
    }

    public void setSku_id(Long sku_id) {
        this.sku_id = sku_id;
    }

    public GiltErrorType getType() {
        return type;
    }

    public void setType(GiltErrorType type) {
        this.type = type;
    }
}
