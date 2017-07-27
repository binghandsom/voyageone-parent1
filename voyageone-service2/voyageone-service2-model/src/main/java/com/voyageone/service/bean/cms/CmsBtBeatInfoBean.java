package com.voyageone.service.bean.cms;

import com.voyageone.service.model.cms.CmsBtPromotionModel;
import com.voyageone.service.model.cms.CmsBtTaskJiagepiluModel;
import com.voyageone.service.model.cms.CmsBtTasksModel;
import com.voyageone.service.model.cms.enums.jiagepilu.BeatFlag;
import com.voyageone.service.model.cms.enums.jiagepilu.ImageStatus;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by jonasvlag on 16/2/29.
 *
 * @version 2.0.0
 * @since 2.0.0
 */
public class CmsBtBeatInfoBean extends CmsBtTaskJiagepiluModel {


    private CmsBtTasksModel task;

    /**
     * 为任务保存信息。
     * <p>该属性为业务过程提供多条信息的报错功能。</p>
     */
    public void setMessage(String message) {
        if (StringUtils.isEmpty(this.message))
            this.message = message;
        else if (!StringUtils.isEmpty(message))
            this.message += ";" + message;
    }

    public CmsBtBeatInfoBean() {
    }

    public CmsBtBeatInfoBean(CmsBtTaskJiagepiluModel model) {
        setId(model.getId());
        setTaskId(model.getTaskId());
        setNumIid(model.getNumIid());
        setProductCode(model.getProductCode());
        setSynFlag(model.getSynFlag());
        setMessage(model.getMessage());
        setImageStatus(model.getImageStatus());
        setImageTaskId(model.getImageTaskId());
        setCreater(model.getCreater());
        setCreated(model.getCreated());
        setModifier(model.getModifier());
        setModified(model.getModified());
    }

    /**
     * 重置(清空)任务信息
     */
    public void clearMessage() {
        this.message = null;
    }

    public BeatFlag getSynFlagEnum() {
        return BeatFlag.valueOf(synFlag);
    }

    public void setSynFlag(BeatFlag synFlag) {
        this.synFlag = synFlag.getFlag();
    }

    public ImageStatus getImageStatusEnum() {
        return imageStatus == null ? ImageStatus.None : ImageStatus.valueOf(imageStatus);
    }

    public void setImageStatus(ImageStatus imageStatus) {
        this.imageStatus = imageStatus.getId();
    }

    public CmsBtTasksModel getTask() {
        return task;
    }

    public void setTask(CmsBtTasksModel task) {
        this.task = task;
    }
}
