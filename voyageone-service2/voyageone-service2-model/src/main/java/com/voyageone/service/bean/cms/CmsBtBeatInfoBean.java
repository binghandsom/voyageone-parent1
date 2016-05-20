package com.voyageone.service.bean.cms;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.voyageone.common.Constants;
import com.voyageone.service.model.cms.CmsBtPromotionModel;
import com.voyageone.service.model.cms.CmsBtTaskJiagepiluModel;
import com.voyageone.service.model.cms.enums.BeatFlag;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by jonasvlag on 16/2/29.
 *
 * @version 2.0.0
 * @since 2.0.0
 */
public class CmsBtBeatInfoBean extends CmsBtTaskJiagepiluModel {

    private int task_id;

    private long num_iid;

    private String product_code;

    private int syn_flag;

    private String message = Constants.EmptyString;

    private CmsBtPromotionCodesBean promotion_code;

    private CmsBtPromotionModel promotion;

    private CmsBtTasksBean task;

    public int getTask_id() {
        return task_id;
    }

    public void setTask_id(int task_id) {
        this.task_id = task_id;
    }

    public long getNum_iid() {
        return num_iid;
    }

    public void setNum_iid(long num_iid) {
        this.num_iid = num_iid;
    }

    public String getProduct_code() {
        return product_code;
    }

    public void setProduct_code(String product_code) {
        this.product_code = product_code;
    }

    public int getSyn_flag() {
        return syn_flag;
    }

    public void setSyn_flag(int syn_flag) {
        this.syn_flag = syn_flag;
    }

    @JsonIgnore
    public BeatFlag getBeatFlag() {
        return BeatFlag.valueOf(getSynFlag());
    }

    @JsonIgnore
    public void setBeatFlag(BeatFlag flag) {
        setSynFlag(flag.getFlag());
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        if (StringUtils.isEmpty(this.message))
            this.message = message;
        else if (!StringUtils.isEmpty(message))
            this.message += ";" + message;
    }

    public void clearMessage() {
        this.message = null;
    }

    public CmsBtPromotionCodesBean getPromotion_code() {
        return promotion_code;
    }

    public void setPromotion_code(CmsBtPromotionCodesBean promotion_code) {
        this.promotion_code = promotion_code;
    }

    public CmsBtPromotionModel getPromotion() {
        return promotion;
    }

    public void setPromotion(CmsBtPromotionModel promotion) {
        this.promotion = promotion;
    }

    public CmsBtTasksBean getTask() {
        return task;
    }

    public void setTask(CmsBtTasksBean task) {
        this.task = task;
    }
}
