package com.voyageone.service.bean.cms.task.beat;

import com.voyageone.common.configs.Enums.PromotionTypeEnums;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.bean.cms.CmsBtTasksBean;
import com.voyageone.service.model.cms.CmsBtPromotionModel;
import com.voyageone.service.model.cms.CmsBtTasksModel;

import java.util.Date;

/**
 * Created by jonasvlag on 16/2/29.
 *
 * @version 2.0.0
 * @since 2.0.0
 */
public class TaskBean {

    private int id;

    private String taskName;

    private PromotionTypeEnums.Type taskType;

    private int promotionId;

    private String channelId;

    /**
     * 价格披露和Promotion解耦没关系了，绑定平台ID
     */
    private Integer cartId;

    private String activityStart;

    private String activityEnd;

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
        setId(taskModel.getId());
        setTaskName(taskModel.getTaskName());
        setTaskType(PromotionTypeEnums.Type.valueOf(taskModel.getTaskType()));
        setPromotionId(taskModel.getPromotionId());
        setChannelId(taskModel.getChannelId());
        setCartId(taskModel.getCartId());
        setActivityStart(taskModel.getActivityStart());
        setActivityEnd(taskModel.getActivityEnd());
        setConfig(JacksonUtil.json2Bean(taskModel.getConfig(), ConfigBean.class));
        setCreated(taskModel.getCreated());
        setCreater(taskModel.getCreater());
        setModified(taskModel.getModified());
        setModifier(taskModel.getModifier());
        setPromotion(taskModel.getPromotion());

        setConfig(JacksonUtil.json2Bean(taskModel.getConfig(), ConfigBean.class));
    }

    public TaskBean(CmsBtTasksModel taskModel) {
        setId(taskModel.getId());
        setTaskName(taskModel.getTaskName());
        setTaskType(PromotionTypeEnums.Type.valueOf(taskModel.getTaskType()));
        setPromotionId(taskModel.getPromotionId());
        setChannelId(taskModel.getChannelId());
        setCartId(taskModel.getCartId());
        setActivityStart(taskModel.getActivityStart());
        setActivityEnd(taskModel.getActivityEnd());
        setConfig(JacksonUtil.json2Bean(taskModel.getConfig(), ConfigBean.class));
        setCreated(taskModel.getCreated());
        setCreater(taskModel.getCreater());
        setModified(taskModel.getModified());
        setModifier(taskModel.getModifier());

        setConfig(JacksonUtil.json2Bean(taskModel.getConfig(), ConfigBean.class));
    }

    public int getId() {
        return id;
    }

    public void setId(int task_id) {
        this.id = task_id;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public PromotionTypeEnums.Type getTaskType() {
        return taskType;
    }

    public void setTaskType(PromotionTypeEnums.Type task_type) {
        this.taskType = task_type;
    }

    public int getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(int promotion_id) {
        this.promotionId = promotion_id;
    }

    public String getActivityStart() {
        return activityStart;
    }

    public void setActivityStart(String activityStart) {
        this.activityStart = activityStart;
    }

    public String getActivityEnd() {
        return activityEnd;
    }

    public void setActivityEnd(String activityEnd) {
        this.activityEnd = activityEnd;
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

        taskModel.setId(getId());
        taskModel.setTaskName(getTaskName());
        taskModel.setTaskType(getTaskType().getTypeId());
        taskModel.setPromotionId(getPromotionId());
        taskModel.setChannelId(getChannelId());
        taskModel.setCartId(getCartId());
        taskModel.setActivityStart(getActivityStart());
        taskModel.setActivityEnd(getActivityEnd());
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

    public Integer getCartId() {
        return cartId;
    }

    public void setCartId(Integer cartId) {
        this.cartId = cartId;
    }

    public Boolean getUpdate() {
        return update;
    }

    public void setUpdate(Boolean update) {
        this.update = update;
    }
}
