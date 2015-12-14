package com.voyageone.batch.cms.bean.tcb;

import com.voyageone.batch.cms.bean.PlatformUploadRunState;
import com.voyageone.batch.cms.bean.UploadImageResult;
import com.voyageone.batch.cms.model.WorkLoadBean;
import com.voyageone.batch.cms.service.rule_parser.ExpressionParser;
import com.voyageone.cms.service.model.CmsMtPlatformCategorySchemaModel;
import com.voyageone.cms.service.model.CmsMtPlatformMappingModel;

/**
 * Created by Leo on 15-6-9.
 */
public class UploadProductTcb extends  TaskControlBlock{
    //上传新产品时业务状态
    private WorkLoadBean workLoadBean;  //全局
    private String platformCId; //全局
    private String numId;  //全局，上新后返回的numId

    private ExpressionParser expressionParser;
    private PlatformUploadRunState platformUploadRunState;

    //关联的UploadImageHandler运行的tcb
    private UploadImageTcb uploadImageTcb;

    private UploadImageResult uploadImageResult;

    public UploadProductTcb(WorkLoadBean workLoadBean) {
        this.workLoadBean = workLoadBean;
    }

    public PlatformUploadRunState getPlatformUploadRunState() {
        return platformUploadRunState;
    }

    public void setPlatformUploadRunState(PlatformUploadRunState platformUploadRunState) {
        this.platformUploadRunState = platformUploadRunState;
    }

    public UploadImageResult getUploadImageResult() {
        return uploadImageResult;
    }

    public void setUploadImageResult(UploadImageResult uploadImageResult) {
        this.uploadImageResult = uploadImageResult;
    }

    public String getNumId() {
        return numId;
    }

    public void setNumId(String numId) {
        this.numId = numId;
    }

    public WorkLoadBean getWorkLoadBean() {
        return workLoadBean;
    }

    public void setWorkLoadBean(WorkLoadBean workLoadBean) {
        this.workLoadBean = workLoadBean;
    }

    public UploadImageTcb getUploadImageTcb() {
        return uploadImageTcb;
    }

    public void setUploadImageTcb(UploadImageTcb uploadImageTcb) {
        this.uploadImageTcb = uploadImageTcb;
    }

    public String getPlatformCId() {
        return platformCId;
    }

    public void setPlatformCId(String platformCId) {
        this.platformCId = platformCId;
    }

    public ExpressionParser getExpressionParser() {
        return expressionParser;
    }

    public void setExpressionParser(ExpressionParser expressionParser) {
        this.expressionParser = expressionParser;
    }
}
