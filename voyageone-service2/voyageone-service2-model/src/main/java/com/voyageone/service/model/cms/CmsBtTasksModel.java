package com.voyageone.service.model.cms;

import com.voyageone.base.dao.mysql.BaseModel;


/**
 * Created by jonasvlag on 16/2/29.
 *
 * @version 2.0.0
 * @since 2.0.0
 */
public class CmsBtTasksModel extends BaseModel {

    private int task_id;

    private String task_name;

    private int task_type;

    private int promotion_id;

    private String  channelId;

    private Integer status;

    private String activity_start;

    private String activity_end;

    private String config;

    private CmsBtPromotionModel promotion;

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

    public int getTask_type() {
        return task_type;
    }

    public void setTask_type(int task_type) {
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

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    public CmsBtPromotionModel getPromotion() {
        return promotion;
    }

    public void setPromotion(CmsBtPromotionModel promotion) {
        this.promotion = promotion;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }
}
