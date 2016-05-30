package com.voyageone.service.bean.cms;

import com.voyageone.service.model.cms.CmsBtPromotionModel;
import com.voyageone.service.model.cms.CmsBtTasksModel;

/**
 * Created by jonasvlag on 16/2/29.
 *
 * @version 2.0.0
 * @since 2.0.0
 */
public class CmsBtTasksBean extends CmsBtTasksModel {

    private CmsBtPromotionModel promotion;

    public CmsBtPromotionModel getPromotion() {
        return promotion;
    }

    public void setPromotion(CmsBtPromotionModel promotion) {
        this.promotion = promotion;
    }
}
