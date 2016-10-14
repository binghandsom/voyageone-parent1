package com.voyageone.service.bean.cms.jumei;

import com.voyageone.service.model.cms.CmsBtJmPromotionModel;
import com.voyageone.service.model.cms.CmsBtTagModel;

import java.util.List;
import java.util.Map;

/**
 * Created by dell on 2016/5/25.
 */
public class CmsBtJmPromotionSaveBean {

    private CmsBtJmPromotionModel model;
    private Map<String, Object> extModel;
    private List<CmsBtTagModel> tagList;
    //活动预热是否开始
    private boolean isBeginPre;
    //活动是否结束
    private boolean isEnd;
    // 是否有扩展信息
    private boolean hasExt;

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

    public boolean getIsBeginPre() {
        return isBeginPre;
    }

    public void setIsBeginPre(boolean beginPre) {
        isBeginPre = beginPre;
    }

    public boolean getIsEnd() {
        return isEnd;
    }

    public void setIsEnd(boolean end) {
        isEnd = end;
    }

    public Map<String, Object> getExtModel() {
        return extModel;
    }

    public void setExtModel(Map<String, Object> extModel) {
        this.extModel = extModel;
    }

    public boolean isHasExt() {
        return hasExt;
    }

    public void setHasExt(boolean hasExt) {
        this.hasExt = hasExt;
    }
}
