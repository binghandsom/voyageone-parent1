package com.voyageone.service.model.cms;

import com.voyageone.base.dao.mysql.BaseModel;

public class CmsBtTaskJiagepiluModel extends BaseModel {

    /**

     */
    private int taskId;
    /**

     */
    private long numIid;
    /**

     */
    private String productCode;
    /**

     */
    private int synFlag;
    /**

     */
    private String message;


    /**

     */
    public int getTaskId() {

        return this.taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }


    /**

     */
    public long getNumIid() {

        return this.numIid;
    }

    public void setNumIid(long numIid) {
        this.numIid = numIid;
    }


    /**

     */
    public String getProductCode() {

        return this.productCode;
    }

    public void setProductCode(String productCode) {
        if (productCode != null) {
            this.productCode = productCode;
        } else {
            this.productCode = "";
        }

    }


    /**

     */
    public int getSynFlag() {

        return this.synFlag;
    }

    public void setSynFlag(int synFlag) {
        this.synFlag = synFlag;
    }


    /**

     */
    public String getMessage() {

        return this.message;
    }

    public void setMessage(String message) {
        if (message != null) {
            this.message = message;
        } else {
            this.message = "";
        }

    }

}