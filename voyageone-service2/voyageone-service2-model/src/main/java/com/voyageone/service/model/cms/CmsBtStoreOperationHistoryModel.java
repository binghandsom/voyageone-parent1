package com.voyageone.service.model.cms;

import com.voyageone.base.dao.mysql.BaseModel;

public class CmsBtStoreOperationHistoryModel extends BaseModel {

    /**
     * 操作类型,1:重新上新所有商品,2:重新导入所有feed商品
     */
    private int operationType;





    /**
     * 操作类型,1:重新上新所有商品,2:重新导入所有feed商品
     */
    public int getOperationType() {

        return this.operationType;
    }

    public void setOperationType(int operationType) {
        this.operationType = operationType;
    }


}
