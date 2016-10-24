package com.voyageone.service.bean.cms.jumei;

import com.voyageone.service.bean.cms.CmsBtTagBean;
import com.voyageone.service.model.cms.CmsBtJmPromotionModel;
import com.voyageone.service.model.cms.CmsBtJmPromotionSpecialExtensionModel;
import com.voyageone.service.model.cms.CmsBtTagModel;

import java.util.List;

/**
 * Created by dell on 2016/5/25.
 */
public class CmsBtJmPromotionSaveBean {

    private CmsBtJmPromotionModel model;
    private CmsBtJmPromotionSpecialExtensionModel extModel;
    private List<CmsBtTagModel> tagList;

    // 是否要处理扩展信息
    private boolean hasExt;
    // 操作类型 1:提交/保存 0:暂存
    private int saveType;

    /**
     * 是否有主推模块
     */
    private boolean hasFeaturedModule;

    public boolean isHasFeaturedModule() {
        return hasFeaturedModule;
    }

    public void setHasFeaturedModule(boolean hasFeaturedModule) {
        this.hasFeaturedModule = hasFeaturedModule;
    }

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
