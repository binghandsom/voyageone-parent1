package com.voyageone.components.cnn.response.data;

import com.voyageone.common.CmsConstants;

/**
 * Created by morse on 2017/7/31.
 */
public class ProductGetStatusResDataBean implements DataBean {

    private static final int IN_STOCK = 0;
    private static final int ON_SALE = 1;

    private int status;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public CmsConstants.PlatformStatus getPlatformStatus() {
        if (status == IN_STOCK) {
            return CmsConstants.PlatformStatus.InStock;
        } else if (status == ON_SALE) {
            return CmsConstants.PlatformStatus.OnSale;
        } else {
            return null;
        }
    }
}
