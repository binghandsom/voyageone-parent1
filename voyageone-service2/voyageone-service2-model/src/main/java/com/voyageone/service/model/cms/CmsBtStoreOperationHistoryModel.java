package com.voyageone.service.model.cms;

import java.io.Serializable;
import java.sql.Timestamp;

public class CmsBtStoreOperationHistoryModel implements Serializable {
    public CmsBtStoreOperationHistoryModel() {
        setModifier("");
        setCreater("");

    }


    /**

     */
    private int id;


    /**
     * 操作类型,1:重新上新所有商品,2:重新导入所有feed商品
     */
    private int operationType;


    /**

     */
    private Timestamp modified;


    /**

     */
    private String modifier;


    /**
     * 创建时间
     */
    private Timestamp created;


    /**

     */
    private String creater;


    /**

     */
    public int getId() {

        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }


    /**
     * 操作类型,1:重新上新所有商品,2:重新导入所有feed商品
     */
    public int getOperationType() {

        return this.operationType;
    }

    public void setOperationType(int operationType) {
        this.operationType = operationType;
    }


    /**

     */
    public Timestamp getModified() {

        return this.modified;
    }

    public void setModified(Timestamp modified) {
        this.modified = modified;
    }


    /**

     */
    public String getModifier() {

        return this.modifier;
    }

    public void setModifier(String modifier) {
        if (modifier != null) {
            this.modifier = modifier;
        } else {
            this.modifier = "";
        }

    }


    /**
     * 创建时间
     */
    public Timestamp getCreated() {

        return this.created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }


    /**

     */
    public String getCreater() {

        return this.creater;
    }

    public void setCreater(String creater) {
        if (creater != null) {
            this.creater = creater;
        } else {
            this.creater = "";
        }

    }

}
