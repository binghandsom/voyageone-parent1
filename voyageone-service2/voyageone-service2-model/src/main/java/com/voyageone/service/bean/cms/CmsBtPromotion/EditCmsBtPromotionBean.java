package com.voyageone.service.bean.cms.CmsBtPromotion;

import com.voyageone.service.model.cms.CmsBtPromotionModel;
import com.voyageone.service.model.cms.CmsBtTagModel;

import java.util.List;

/**
 * Created by dell on 2016/7/14.
 */
public class EditCmsBtPromotionBean {
    public CmsBtPromotionModel getPromotionModel() {
        return promotionModel;
    }

    public void setPromotionModel(CmsBtPromotionModel promotionModel) {
        this.promotionModel = promotionModel;
    }

    public List<CmsBtTagModel> getTagList() {
        return tagList;
    }

    public void setTagList(List<CmsBtTagModel> tagList) {
        this.tagList = tagList;
    }

    private CmsBtPromotionModel promotionModel;
    private List<CmsBtTagModel> tagList;
}
