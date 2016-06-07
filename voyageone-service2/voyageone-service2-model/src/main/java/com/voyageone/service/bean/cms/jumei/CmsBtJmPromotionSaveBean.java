package com.voyageone.service.bean.cms.jumei;

import com.voyageone.service.model.cms.CmsBtJmPromotionModel;
import com.voyageone.service.model.cms.CmsBtTagModel;

import java.util.List;

/**
 * Created by dell on 2016/5/25.
 */
public class CmsBtJmPromotionSaveBean {
    private CmsBtJmPromotionModel model;
    private List<CmsBtTagModel> tagList;

    public CmsBtJmPromotionModel getModel() {
        return model;
    }

    public void setModel(CmsBtJmPromotionModel model) {
        this.model = model;
    }

    public List<CmsBtTagModel> getTagList() {
        return tagList;
    }

    public void setTagList(List<CmsBtTagModel> tagList) {
        this.tagList = tagList;
    }
}
