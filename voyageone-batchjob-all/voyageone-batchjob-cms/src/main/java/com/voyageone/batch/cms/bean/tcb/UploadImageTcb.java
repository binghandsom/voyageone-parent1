package com.voyageone.batch.cms.bean.tcb;

import com.voyageone.batch.cms.bean.UploadImageParam;
import com.voyageone.batch.cms.bean.UploadImageResult;

/**
 * Created by Leo on 15-6-9.
 */
public class UploadImageTcb extends TaskControlBlock{
    private UploadImageParam uploadImageParam;
    private UploadImageResult uploadImageResult;
    private UploadProductTcb uploadProductTcb;

    public UploadProductTcb getUploadProductTcb() {
        return uploadProductTcb;
    }

    public void setUploadProductTcb(UploadProductTcb uploadProductTcb) {
        this.uploadProductTcb = uploadProductTcb;
    }

    public UploadImageTcb(UploadImageParam uploadImageParam) {
        this.uploadImageParam = uploadImageParam;
    }

    public UploadImageParam getUploadImageParam() {
        return uploadImageParam;
    }

    public void setUploadImageParam(UploadImageParam uploadImageParam) {
        this.uploadImageParam = uploadImageParam;
    }

    public UploadImageResult getUploadImageResult() {
        return uploadImageResult;
    }

    public void setUploadImageResult(UploadImageResult uploadImageResult) {
        this.uploadImageResult = uploadImageResult;
    }
}
