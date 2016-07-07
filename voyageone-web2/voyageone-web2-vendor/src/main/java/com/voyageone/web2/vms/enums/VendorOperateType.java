package com.voyageone.web2.vms.enums;

/**
 * 渠道操作方式:订单/sku
 * Created by vantis on 16-7-7.
 */
public enum VendorOperateType {
    SKU("SKU"),
    ORDER("ORDER");

    private String value;

    VendorOperateType(String value) {
        this.value = value;
    }
}
