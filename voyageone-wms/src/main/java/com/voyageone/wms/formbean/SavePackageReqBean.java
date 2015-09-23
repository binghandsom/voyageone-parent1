package com.voyageone.wms.formbean;

import com.voyageone.wms.modelbean.TransferDetailBean;
import com.voyageone.wms.modelbean.TransferItemBean;

import java.util.List;

/**
 * A AjaxRequestBean
 * Created by Tester on 4/30/2015.
 * @author Jonas
 */
public class SavePackageReqBean {
    private TransferDetailBean pkg;

    private List<TransferItemBean> items;

    public TransferDetailBean getPkg() {
        return pkg;
    }

    public void setPkg(TransferDetailBean pkg) {
        this.pkg = pkg;
    }

    public List<TransferItemBean> getItems() {
        return items;
    }

    public void setItems(List<TransferItemBean> items) {
        this.items = items;
    }
}
