package com.voyageone.service.model.cms;

import com.voyageone.base.dao.mysql.BaseModel;

public class CmsMtImageCreateTaskModel extends BaseModel {

    /**

     */
    private String name;
    /**
     * 1:处理完成
     */
    private int status;


    /**

     */
    public String getName() {

        return this.name;
    }

    public void setName(String name) {
        if (name != null) {
            this.name = name;
        } else {
            this.name = "";
        }

    }


    /**
     * 1:处理完成
     */
    public int getStatus() {

        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }


}