package com.voyageone.service.bean.cms.businessmodel.PromotionProduct;

import com.voyageone.common.configs.beans.TypeChannelBean;
import com.voyageone.service.model.cms.CmsBtJmPromotionModel;
import com.voyageone.service.model.cms.CmsBtTagModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dell on 2016/6/30.
 */
public class InitResult {
    CmsBtJmPromotionModel modelPromotion;
    List<CmsBtTagModel> listTag;//活动的tag
    int changeCount;//变更数量
    boolean isBegin;//活动是否开始
    boolean isEnd;//活动是否结束
    boolean isUpdateJM;//9：00 12：00 是否更新聚美
    //boolean isBefore5DaysBeforePreBegin;//是否是预热开始前5天之前

    public boolean isBegin() {
        return isBegin;
    }

    public void setBegin(boolean begin) {
        isBegin = begin;
    }

    public boolean isEnd() {
        return isEnd;
    }

    public void setEnd(boolean end) {
        isEnd = end;
    }

    public boolean isUpdateJM() {
        return isUpdateJM;
    }

    public void setUpdateJM(boolean updateJM) {
        isUpdateJM = updateJM;
    }

//    public boolean getIsBefore5DaysBeforePreBegin() {
//        return isBefore5DaysBeforePreBegin;
//    }

//    public void setIsBefore5DaysBeforePreBegin(boolean isBefore5DaysBeforePreBegin) {
//        isBefore5DaysBeforePreBegin = isBefore5DaysBeforePreBegin;
//    }

    List<TypeChannelBean> brandList = new ArrayList<>();

    public boolean getIsBegin() {
        return isBegin;
    }

    public void setIsBegin(boolean begin) {
        isBegin = begin;
    }

    public boolean getIsEnd() {
        return isEnd;
    }

    public void setIsEnd(boolean end) {
        isEnd = end;
    }

    public boolean getIsUpdateJM() {
        return isUpdateJM;
    }

    public void setIsUpdateJM(boolean updateJM) {
        isUpdateJM = updateJM;
    }


    public CmsBtJmPromotionModel getModelPromotion() {
        return modelPromotion;
    }

    public void setModelPromotion(CmsBtJmPromotionModel modelPromotion) {
        this.modelPromotion = modelPromotion;
    }

    public List<CmsBtTagModel> getListTag() {
        return listTag;
    }

    public void setListTag(List<CmsBtTagModel> listTag) {
        this.listTag = listTag;
    }

    public int getChangeCount() {
        return changeCount;
    }

    public void setChangeCount(int changeCount) {
        this.changeCount = changeCount;
    }

    public List<TypeChannelBean> getBrandList() {
        return brandList;
    }

    public void setBrandList(List<TypeChannelBean> brandList) {
        this.brandList = brandList;
    }
}
