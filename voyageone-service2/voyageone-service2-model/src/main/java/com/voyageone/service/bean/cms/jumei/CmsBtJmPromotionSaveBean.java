package com.voyageone.service.bean.cms.jumei;

import com.voyageone.service.model.cms.CmsBtJmPromotionModel;
import com.voyageone.service.model.cms.CmsBtJmPromotionSpecialExtensionModel;
import com.voyageone.service.model.cms.CmsBtTagModel;

import java.util.List;
import java.util.Map;

/**
 * Created by dell on 2016/5/25.
 */
public class CmsBtJmPromotionSaveBean {

    private CmsBtJmPromotionModel model;
    private CmsBtJmPromotionSpecialExtensionModel extModel;
    private List<CmsBtTagModel> tagList;
    //活动预热是否开始
    private boolean isBeginPre;
    //活动是否结束
    private boolean isEnd;
    // 是否有扩展信息
    private boolean hasExt;
    // 操作类型 1:提交/保存 0:暂存
    private int saveType;

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

    public CmsBtJmPromotionSpecialExtensionModel getExtModel() {
        return extModel;
    }

    public void setExtModel(CmsBtJmPromotionSpecialExtensionModel extModel) {
        this.extModel = extModel;
    }

    public boolean isHasExt() {
        return hasExt;
    }

    public void setHasExt(boolean hasExt) {
        this.hasExt = hasExt;
    }

    public int getSaveType() {
        return saveType;
    }

    public void setSaveType(int saveType) {
        this.saveType = saveType;
    }
}
