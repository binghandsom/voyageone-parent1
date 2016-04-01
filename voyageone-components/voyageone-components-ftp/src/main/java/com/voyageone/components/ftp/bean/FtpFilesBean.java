package com.voyageone.components.ftp.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * FtpFilesBean
 *
 * @author chuanyu.liang 2016/5/10.
 * @version 2.0.0
 * @since 2.0.0
 */
public class FtpFilesBean extends BaseFtpBean {

    private List<FtpSubFileBean> fileBeans = new ArrayList<>();

    public FtpFilesBean(BaseFtpBean baseFtpBean) {
        super(baseFtpBean);
    }

    public List<FtpSubFileBean> getFileBeans() {
        return fileBeans;
    }

    public void setFileBeans(List<FtpSubFileBean> fileBeans) {
        this.fileBeans = fileBeans;
    }

    public void addFileBean(FtpSubFileBean file) {
        fileBeans.add(file);
    }

    public void checkListParam() {
        super.checkParam();
        for (FtpSubFileBean ftpFileBean : fileBeans) {
            ftpFileBean.checkListParam();
        }
    }

    public void checkUploadParam() {
        super.checkParam();
        for (FtpSubFileBean ftpFileBean : fileBeans) {
            ftpFileBean.checkUploadParam();
        }
    }

    public void checkDownloadParam() {
        super.checkParam();
        for (FtpSubFileBean ftpFileBean : fileBeans) {
            ftpFileBean.checkDownloadParam();
        }
    }

    public void checkDeleteParam() {
        super.checkParam();
        for (FtpSubFileBean ftpFileBean : fileBeans) {
            ftpFileBean.checkDeleteParam();
        }
    }
}
