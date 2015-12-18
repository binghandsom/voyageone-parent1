package com.voyageone.batch.cms.bean;

import com.voyageone.batch.cms.bean.tcb.UploadProductTcb;

/**
 * Created by Leo on 15-7-10.
 */
public abstract class PlatformUploadRunState {
    protected UploadProductTcb uploadProductTcb;

    public abstract class PlatformContextBuildFields {
        protected PlatformUploadRunState platformUploadRunState;
        protected PlatformContextBuildCustomFields platformContextBuildCustomFields;

        public PlatformContextBuildCustomFields getPlatformContextBuildCustomFields() {
            return platformContextBuildCustomFields;
        }

        public void setPlatformContextBuildCustomFields(PlatformContextBuildCustomFields platformContextBuildCustomFields) {
            this.platformContextBuildCustomFields = platformContextBuildCustomFields;
        }

        public PlatformUploadRunState getPlatformUploadRunState() {
            return platformUploadRunState;
        }

        public void setPlatformUploadRunState(PlatformUploadRunState platformUploadRunState) {
            this.platformUploadRunState = platformUploadRunState;
        }
    }

    public abstract class PlatformContextBuildCustomFields {
        protected PlatformContextBuildFields platformContextBuildFields;

        public PlatformContextBuildFields getPlatformContextBuildFields() {
            return platformContextBuildFields;
        }

        public void setPlatformContextBuildFields(PlatformContextBuildFields platformContextBuildFields) {
            this.platformContextBuildFields = platformContextBuildFields;
        }
    }

    public UploadProductTcb getUploadProductTcb() {
        return uploadProductTcb;
    }

    public void setUploadProductTcb(UploadProductTcb uploadProductTcb) {
        this.uploadProductTcb = uploadProductTcb;
    }
}
