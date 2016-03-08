package com.voyageone.web2.cms.wsdl.models;

import com.voyageone.cms.enums.BeatFlag;
import com.voyageone.common.Constants;
import com.voyageone.web2.sdk.api.domain.CmsBtPromotionCodeModel;
import com.voyageone.web2.sdk.api.domain.CmsBtPromotionModel;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by jonasvlag on 16/2/29.
 *
 * @version 2.0.0
 * @since 2.0.0
 */
public class CmsBtBeatInfoModel {

    private int id;

    private int task_id;

    private long num_iid;

    private String product_code;

    private int syn_flag;

    private String message = Constants.EmptyString;

    private String created;

    private String creater;

    private String modified;

    private String modifier;

    private CmsBtPromotionCodeModel promotion_code;

    private CmsBtPromotionModel promotion;

    private CmsBtTaskModel task;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public void setBeatFlag(BeatFlag flag) {
        setSyn_flag(flag.getFlag());
    }

    public BeatFlag getBeatFlag() {
        return BeatFlag.valueOf(getSyn_flag());
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        if (this.message == null)
            this.message = message;
        else if (!StringUtils.isEmpty(message))
            this.message += ";" + message;
    }

    public void clearMessage() {
        this.message = "";
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

    public CmsBtPromotionCodeModel getPromotion_code() {
        return promotion_code;
    }

    public void setPromotion_code(CmsBtPromotionCodeModel promotion_code) {
        this.promotion_code = promotion_code;
    }

    public CmsBtPromotionModel getPromotion() {
        return promotion;
    }

    public void setPromotion(CmsBtPromotionModel promotion) {
        this.promotion = promotion;
    }

    public CmsBtTaskModel getTask() {
        return task;
    }

    public void setTask(CmsBtTaskModel task) {
        this.task = task;
    }
}
