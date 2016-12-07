package com.voyageone.components.sneakerhead.bean.platformstatus.usPlatformModel;


import com.voyageone.components.sneakerhead.enums.UsPlatformStatus;

/**
 * Created by vantis on 2016/12/2.
 * 闲舟江流夕照晚 =。=
 */
public class DbCodeUsPlatformStatusModel {
    private String code;
    private UsPlatformStatus magento;
    private UsPlatformStatus amazon;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public UsPlatformStatus getMagento() {
        return magento;
    }

    public void setMagento(UsPlatformStatus magento) {
        this.magento = magento;
    }

    public UsPlatformStatus getAmazon() {
        return amazon;
    }

    public void setAmazon(UsPlatformStatus amazon) {
        this.amazon = amazon;
    }
}
