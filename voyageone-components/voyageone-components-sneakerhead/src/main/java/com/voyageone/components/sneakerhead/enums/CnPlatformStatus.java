package com.voyageone.components.sneakerhead.enums;

/**
 * Created by vantis on 2016/12/1.
 * 闲舟江流夕照晚 =。=
 */
public enum CnPlatformStatus {
    OnSale("1"),
    InStock("0");

    private String value;

    CnPlatformStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

}
