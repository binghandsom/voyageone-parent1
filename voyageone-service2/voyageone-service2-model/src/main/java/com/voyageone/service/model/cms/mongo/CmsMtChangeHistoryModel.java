package com.voyageone.service.model.cms.mongo;

import com.voyageone.base.dao.mongodb.model.BaseMongoModel;

/**
 * 通用的变更历史表
 *
 * @description
 * @author: holysky.zhao
 * @date: 2016/4/28 11:08
 * COPYRIGHT © 2001 - 2016 VOYAGE ONE GROUP INC. ALL RIGHTS RESERVED.
 */
public class CmsMtChangeHistoryModel<T> extends BaseMongoModel {

    private String operation;

    private T originalBean;   //原始对象
    private T newBean;   //新对象
    private String typeName;


//    @SuppressWarnings("unchecked")
//    public Class<T> getEntityClass() {
//        return (Class<T>)getSuperClassGenricType(getClass(), 0);
//    }


    private CmsMtChangeHistoryModel() {
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public T getOriginalBean() {
        return originalBean;
    }

    public void setOriginalBean(T originalBean) {
        if (typeName == null) {
            typeName = originalBean != null ? originalBean.getClass().getCanonicalName() : null;
        }
        this.originalBean = originalBean;
    }

    public T getNewBean() {
        return newBean;
    }

    public void setNewBean(T newBean) {
        if (typeName == null) {
            typeName = originalBean != null ? originalBean.getClass().getCanonicalName() : null;
        }
        this.newBean = newBean;
    }

    /**
     * 唯一的构造方式,防止误用
     * @param orginBean
     * @param newBean
     * @param operation
     * @param modifier
     * @param <T>
     * @return
     */
    public static <T> CmsMtChangeHistoryModel<T> build(T orginBean, T newBean,
                                                       String operation,
                                                       String modifier){
        CmsMtChangeHistoryModel historyModel = new CmsMtChangeHistoryModel();
        historyModel.setOperation(operation);
        historyModel.setCreated(modifier);
        historyModel.setModifier(modifier);
        historyModel.setOriginalBean(orginBean);
        historyModel.setNewBean(newBean);
        return historyModel;
    }
}
