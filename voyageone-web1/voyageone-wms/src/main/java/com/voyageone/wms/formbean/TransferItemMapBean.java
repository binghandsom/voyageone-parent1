package com.voyageone.wms.formbean;

import com.voyageone.wms.modelbean.TransferItemBean;

/**
 * 包含映射关系的 TransferItem Bean
 * Created by Tester on 1/23/2016.
 * @author Jack
 */
public class TransferItemMapBean {
    public TransferItemBean transferItem;

    public String context_name;

    public TransferItemBean context;

    public TransferItemBean getTransferItem() {
        return transferItem;
    }

    public void setTransferItem(TransferItemBean transferItem) {
        this.transferItem = transferItem;
    }

    public String getContext_name() {
        return context_name;
    }

    public void setContext_name(String context_name) {
        this.context_name = context_name;
    }

    public TransferItemBean getContext() {
        return context;
    }

    public void setContext(TransferItemBean context) {
        this.context = context;
    }
}
