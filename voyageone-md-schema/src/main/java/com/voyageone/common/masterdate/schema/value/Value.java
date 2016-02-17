package com.voyageone.common.masterdate.schema.value;

public class Value {
    protected String id;
    protected String value;

    public Value() {
    }

    public Value(String value) {
        this.value = value;
    }

    public Value(String id, String value) {
        this.value = value;
        this.id = id;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
