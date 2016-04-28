package com.voyageone.service.model.cms;

import com.voyageone.base.dao.mysql.BaseModel;

public class CmsBtStoreOperationHistoryModel extends BaseModel {

    /**
     * 操作类型
     */
    private String operationType;


    /**
     * 操作类型,1:重新上新所有商品,2:重新导入所有feed商品
     */
    public String getOperationType() {

        return this.operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }


}
