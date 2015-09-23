package com.voyageone.batch.ims.bean.tcb;

/**
 * Created by Leo on 15-6-30.
 */
public class MainToUploadImageTaskSignalInfo extends TaskSignalInfo {
    private UploadProductTcb uploadProductTcb;
    private UploadImageTcb uploadImageTcb;

    public MainToUploadImageTaskSignalInfo(UploadProductTcb uploadProductTcb, UploadImageTcb uploadImageTcb) {
        this.uploadImageTcb = uploadImageTcb;
        this.uploadProductTcb = uploadProductTcb;
    }

    public UploadProductTcb getUploadProductTcb() {
        return uploadProductTcb;
    }

    public void setUploadProductTcb(UploadProductTcb uploadProductTcb) {
        this.uploadProductTcb = uploadProductTcb;
    }

    public UploadImageTcb getUploadImageTcb() {
        return uploadImageTcb;
    }

    public void setUploadImageTcb(UploadImageTcb uploadImageTcb) {
        this.uploadImageTcb = uploadImageTcb;
    }
}
