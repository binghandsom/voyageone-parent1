package com.voyageone.service.bean.cms.product;

import com.voyageone.common.CmsConstants;
import com.voyageone.common.util.StringUtils;

/**
 * 卖全球Bean
 *
 * @author morse on 2016/12/08
 * @version 2.6.0
 */
public class ProductMqqBean {

    private String mqqName;
    private String numIId;
    private String status;

    public String getMqqName() {
        return mqqName;
    }

    public void setMqqName(String mqqName) {
        this.mqqName = mqqName;
    }

    public String getNumIId() {
        return numIId;
    }

    public void setNumIId(String numIId) {
        this.numIId = numIId;
    }

    // "InStock"(在库)/"OnSale"(在售)
    public CmsConstants.PlatformStatus getStatus() {
        CmsConstants.PlatformStatus rs = null;
        try {
            rs = (StringUtils.isEmpty(status)) ? null : CmsConstants.PlatformStatus.valueOf(status);
        } catch (IllegalArgumentException ignored) {
        }
        return rs;
    }

    public void setStatus(CmsConstants.PlatformStatus status) {
        if (status != null) {
            this.status = status.name();
        } else {
            this.status = null;
        }
    }

}
