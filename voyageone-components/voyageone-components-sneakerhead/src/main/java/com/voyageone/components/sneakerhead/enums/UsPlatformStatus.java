package com.voyageone.components.sneakerhead.enums;

/**
 * Created by vantis on 2016/12/2.
 * 闲舟江流夕照晚 =。=
 */
public enum UsPlatformStatus {
    OnSale("1"),
    InStock("4");

    private String value;

    UsPlatformStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
