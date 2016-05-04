package com.voyageone.service.model.cms;

import com.voyageone.base.dao.mysql.BaseModel;

public class CmsMtCustomWordModel extends BaseModel {

    /**

     */
    private String wordName;
    /**

     */
    private String wordDesc;

    /**

     */
    public String getWordName() {

        return this.wordName;
    }

    public void setWordName(String wordName) {
        if (wordName != null) {
            this.wordName = wordName;
        } else {
            this.wordName = "";
        }

    }


    /**

     */
    public String getWordDesc() {

        return this.wordDesc;
    }

    public void setWordDesc(String wordDesc) {
        if (wordDesc != null) {
            this.wordDesc = wordDesc;
        } else {
            this.wordDesc = "";
        }

    }
}