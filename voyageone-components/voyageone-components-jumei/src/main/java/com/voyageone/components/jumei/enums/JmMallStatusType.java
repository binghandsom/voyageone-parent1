package com.voyageone.components.jumei.enums;

/**
 * 聚美商城 上下架状态
 *
 * @author morse on 2016/8/29
 * @version 2.5.0
 */
public enum JmMallStatusType {
    ToInStock("hidden"),       // 下架
    ToOnSale("display"),       // 上架
    ;

    private String val;

    private JmMallStatusType(String val) {
        this.val = val;
    }

    public String getVal() {
        return val;
    }
}
