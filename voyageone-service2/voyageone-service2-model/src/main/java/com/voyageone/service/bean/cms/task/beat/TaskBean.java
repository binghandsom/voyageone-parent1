package com.voyageone.service.bean.cms.task.beat;

import com.voyageone.common.configs.Enums.PromotionTypeEnums;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.bean.cms.CmsBtTasksBean;
import com.voyageone.service.model.cms.CmsBtPromotionModel;

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

    private String created;

    private String creater;

    private String modified;

    private String modifier;

    private CmsBtPromotionModel promotion;

    public TaskBean() {
    }

    public TaskBean(CmsBtTasksBean taskModel) {
        setTask_id(taskModel.getTask_id());
        setTask_name(taskModel.getTask_name());
        setTask_type(PromotionTypeEnums.Type.valueOf(taskModel.getTask_type()));
        setPromotion_id(taskModel.getPromotion_id());
        setChannelId(taskModel.getChannelId());
        setActivity_start(taskModel.getActivity_start());
        setActivity_end(taskModel.getActivity_end());
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

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
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

        taskModel.setTask_id(getTask_id());
        taskModel.setTask_name(getTask_name());
        taskModel.setTask_type(getTask_type().getTypeId());
        taskModel.setPromotion_id(getPromotion_id());
        taskModel.setChannelId(getChannelId());
        taskModel.setActivity_start(getActivity_start());
        taskModel.setActivity_end(getActivity_end());
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
}
