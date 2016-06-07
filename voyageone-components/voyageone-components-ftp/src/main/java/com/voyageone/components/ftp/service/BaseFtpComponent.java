package com.voyageone.components.ftp.service;

import com.voyageone.common.util.StringUtils;
import com.voyageone.components.ComponentBase;
import com.voyageone.components.ftp.bean.FtpConnectBean;
import com.voyageone.components.ftp.bean.FtpFileBean;

import java.util.List;

/**
 * BaseFtpComponent
 *
 * @author chuanyu.liang 2016/5/10.
 * @version 2.0.0
 * @since 2.0.0
 */
public abstract class BaseFtpComponent extends ComponentBase {

    public final static String seperator = "/";

    protected FtpConnectBean ftpConnectBean;

    public BaseFtpComponent(FtpConnectBean ftpConnectBean) {
        this.ftpConnectBean = ftpConnectBean;
    }

    public FtpConnectBean getFtpConnectBean() {
        return ftpConnectBean;
    }

    public abstract void openConnect();

    public abstract void closeConnect();

    public abstract void uploadFile(FtpFileBean fileBean);

    public void uploadFiles(List<FtpFileBean> fileBeans) {
        fileBeans.forEach(this::uploadFile);
    }

    public abstract void downloadFile(FtpFileBean fileBean);

    public void downloadFiles(List<FtpFileBean> fileBeans) {
        fileBeans.forEach(this::downloadFile);
    }

    public abstract void deleteFile(FtpFileBean fileBean);

    public void deleteFiles(List<FtpFileBean> fileBeans) {
        fileBeans.forEach(this::deleteFile);
    }

    public abstract List<String> listFilePath(String remotePath);

    public abstract boolean changePath(String remotePath);

    public abstract void changeOrCreatePath(String filepath);

    protected abstract boolean isConnection();

    protected String getLocalFilePathName(FtpFileBean fileBean) {
        String localFilePathName = "";
        if (!StringUtils.isEmpty(fileBean.getLocalPath())) {
            localFilePathName = fileBean.getLocalPath();
        }

        if (!localFilePathName.endsWith(seperator)) {
            localFilePathName += seperator;
        }
        localFilePathName += fileBean.getLocalFilename();
        return localFilePathName;
    }

    protected String getRemoteFilePathName(FtpFileBean fileBean) {
        String remoteFilePathName = seperator;
        if (!StringUtils.isEmpty(fileBean.getRemotePath())) {
            remoteFilePathName = fileBean.getRemotePath();
        }

        if (!remoteFilePathName.endsWith(seperator)) {
            remoteFilePathName += seperator;
        }
        remoteFilePathName += fileBean.getRemoteFilename();
        return remoteFilePathName;
    }

    public void enterLocalPassiveMode() {
    }
}
