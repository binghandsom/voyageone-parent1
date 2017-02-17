package com.voyageone.service.model.cms.mongo;

/**
 * @author Edward
 * @version 2.0.0, 2017/2/13
 */
public class CmsBtOperationLogModel_Msg {

    /**
     * sku或者code
     */
    private String skuCode;

    /**
     * 错误原因
     */
    private String msg;

    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
