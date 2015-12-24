package com.voyageone.web2.cms.model;

import com.voyageone.base.dao.mongodb.model.BaseMongoModel;
import com.voyageone.cms.service.model.CmsBtProductModel;

/**
 * @author james.li on 2015/12/15.
 * @version 2.0.0
 */
public class CmsBtPromotionTaskModel extends BaseMongoModel {

    private Integer promotionId;

    private Integer taskType;

    private String key;

    private Integer synFlg;

    private String errMsg;

    public CmsBtPromotionTaskModel() {
        this.synFlg = 0;
        this.errMsg = "";
    }

    public CmsBtPromotionTaskModel(Integer promotionId, Integer taskType, String key, String operator) {
        this();
        this.promotionId = promotionId;
        this.taskType = taskType;
        this.key = key;
        this.setCreater(operator);
        this.setModifier(operator);
    }

    public Integer getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(Integer promotionId) {
        this.promotionId = promotionId;
    }

    public Integer getTaskType() {
        return taskType;
    }

    public void setTaskType(Integer taskType) {
        this.taskType = taskType;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getSynFlg() {
        return synFlg;
    }

    public void setSynFlg(Integer synFlg) {
        this.synFlg = synFlg;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }
}
