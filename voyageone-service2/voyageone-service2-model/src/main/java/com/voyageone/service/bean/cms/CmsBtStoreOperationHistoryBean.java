package com.voyageone.service.bean.cms;

import com.voyageone.service.model.cms.CmsBtStoreOperationHistoryModel;

/**
 * Created by jeff on 2016/5/26.
 */
public class CmsBtStoreOperationHistoryBean extends CmsBtStoreOperationHistoryModel {

    protected String operationTypeName;

    public String getOperationTypeName() {
        return operationTypeName;
    }

    public void setOperationTypeName(String operationTypeName) {
        this.operationTypeName = operationTypeName;
    }

}
