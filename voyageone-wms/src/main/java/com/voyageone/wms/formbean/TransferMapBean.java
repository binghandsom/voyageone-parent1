package com.voyageone.wms.formbean;

import com.voyageone.wms.modelbean.TransferBean;

/**
 * 包含映射关系的 Transfer Bean
 * Created by Tester on 5/5/2015.
 * @author Jonas
 */
public class TransferMapBean {
    public TransferBean transfer;

    public String context_name;

    public TransferBean context;

    public TransferBean getTransfer() {
        return transfer;
    }

    public void setTransfer(TransferBean transfer) {
        this.transfer = transfer;
    }

    public TransferBean getContext() {
        return context;
    }

    public void setContext(TransferBean context) {
        this.context = context;
    }

    public String getContext_name() {
        return context_name;
    }

    public void setContext_name(String context_name) {
        this.context_name = context_name;
    }
}
