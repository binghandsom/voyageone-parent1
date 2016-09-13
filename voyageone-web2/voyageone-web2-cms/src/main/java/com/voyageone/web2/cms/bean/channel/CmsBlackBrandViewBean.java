package com.voyageone.web2.cms.bean.channel;

import com.voyageone.service.model.cms.CmsBtBrandBlockModel;

/**
 * Created by jonas on 9/12/16.
 *
 * @author jonas
 * @version 2.6.0
 * @since 2.6.0
 */
public class CmsBlackBrandViewBean extends CmsBtBrandBlockModel {

    private boolean blocked;

    private String brandName;

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }
}
