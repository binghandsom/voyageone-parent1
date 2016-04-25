package com.voyageone.service.model.cms;

import com.voyageone.base.dao.mysql.BaseModel;

public class CmsMtCustomWordParamModel extends BaseModel {

    /**

     */
    private String paramName;
    /**

     */
    private String paramDesc;
    /**

     */
    private int wordId;

    /**

     */
    public String getParamName() {

        return this.paramName;
    }

    public void setParamName(String paramName) {
        if (paramName != null) {
            this.paramName = paramName;
        } else {
            this.paramName = "";
        }

    }


    /**

     */
    public String getParamDesc() {

        return this.paramDesc;
    }

    public void setParamDesc(String paramDesc) {
        if (paramDesc != null) {
            this.paramDesc = paramDesc;
        } else {
            this.paramDesc = "";
        }

    }


    /**

     */
    public int getWordId() {

        return this.wordId;
    }

    public void setWordId(int wordId) {
        this.wordId = wordId;
    }


}