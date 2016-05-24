package com.voyageone.service.bean.cms.task.beat;

import com.voyageone.common.configs.Enums.PromotionTypeEnums;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.bean.cms.CmsBtTasksBean;
import com.voyageone.service.model.cms.CmsBtPromotionModel;

import java.util.Date;

/**
 * Created by jonasvlag on 16/2/29.
 *
 * @version 2.0.0
 * @since 2.0.0
 */
public class TaskBean {

    private int task_id;

    private String task_name;

    private PromotionTypeEnums.Type task_type;

    private int promotion_id;

    private String channelId;

    private String activity_start;

    private String activity_end;

    private ConfigBean config;

    private Date created;

    private String creater;

    private Date modified;

    private String modifier;

    private CmsBtPromotionModel promotion;

    private Boolean update;

    public TaskBean() {
    }

    public TaskBean(CmsBtTasksBean taskModel) {
        setTask_id(taskModel.getId());
        setTask_name(taskModel.getTaskName());
        setTask_type(PromotionTypeEnums.Type.valueOf(taskModel.getTaskType()));
        setPromotion_id(taskModel.getPromotionId());
        setChannelId(taskModel.getChannelId());
        setActivity_start(taskModel.getActivityStart());
        setActivity_end(taskModel.getActivityEnd());
        setConfig(JacksonUtil.json2Bean(taskModel.getConfig(), ConfigBean.class));
        setCreated(taskModel.getCreated());
        setCreater(taskModel.getCreater());
        setModified(taskModel.getModified());
        setModifier(taskModel.getModifier());
        setPromotion(taskModel.getPromotion());
    }

    public int getTask_id() {
        return task_id;
    }

    public void setTask_id(int task_id) {
        this.task_id = task_id;
    }

    public String getTask_name() {
        return task_name;
    }

    public void setTask_name(String task_name) {
        this.task_name = task_name;
    }

    public PromotionTypeEnums.Type getTask_type() {
        return task_type;
    }

    public void setTask_type(PromotionTypeEnums.Type task_type) {
        this.task_type = task_type;
    }

    public int getPromotion_id() {
        return promotion_id;
    }

    public void setPromotion_id(int promotion_id) {
        this.promotion_id = promotion_id;
    }

    public String getActivity_start() {
        return activity_start;
    }

    public void setActivity_start(String activity_start) {
        this.activity_start = activity_start;
    }

    public String getActivity_end() {
        return activity_end;
    }

    public void setActivity_end(String activity_end) {
        this.activity_end = activity_end;
    }

    public ConfigBean getConfig() {
        return config;
    }

    public void setConfig(ConfigBean config) {
        this.config = config;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater;
    }

    public Date getModified() {
        return modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public CmsBtPromotionModel getPromotion() {
        return promotion;
    }

    public void setPromotion(CmsBtPromotionModel promotion) {
        this.promotion = promotion;
    }

    public CmsBtTasksBean toModel() {

        CmsBtTasksBean taskModel = new CmsBtTasksBean();

        taskModel.setId(getTask_id());
        taskModel.setTaskName(getTask_name());
        taskModel.setTaskType(getTask_type().getTypeId());
        taskModel.setPromotionId(getPromotion_id());
        taskModel.setChannelId(getChannelId());
        taskModel.setActivityStart(getActivity_start());
        taskModel.setActivityEnd(getActivity_end());
        taskModel.setConfig(JacksonUtil.bean2Json(getConfig()));
        taskModel.setCreated(getCreated());
        taskModel.setCreater(getCreater());
        taskModel.setModified(getModified());
        taskModel.setModifier(getModifier());

        return taskModel;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public Boolean getUpdate() {
        return update;
    }

    public void setUpdate(Boolean update) {
        this.update = update;
    }
}
