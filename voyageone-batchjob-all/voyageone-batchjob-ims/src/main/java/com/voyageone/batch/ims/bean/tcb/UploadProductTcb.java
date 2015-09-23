package com.voyageone.batch.ims.bean.tcb;

import com.voyageone.batch.ims.bean.PlatformUploadRunState;
import com.voyageone.batch.ims.bean.UploadImageResult;
import com.voyageone.batch.ims.modelbean.WorkLoadBean;

/**
 * Created by Leo on 15-6-9.
 */
public class UploadProductTcb extends  TaskControlBlock{
    //上传新产品时业务状态
    private WorkLoadBean workLoadBean;  //全局
    private String platformCId; //全局
    //private CmsCodePropBean mainCmsCodeProp;//全局 主产品code的属性
    private String numId;  //全局，上新后返回的numId

    //private UploadProductRunState uploadProductRunState; //针对upload product
    //private UploadItemRunState uploadItemRunState; //针对upload item
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
}
