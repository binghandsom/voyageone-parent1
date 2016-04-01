package com.voyageone.components.ftp.bean;

/**
 * FtpDirectoryBean
 *
 * @author chuanyu.liang 2016/5/10.
 * @version 2.0.0
 * @since 2.0.0
 */
public class FtpDirectoryBean extends BaseFtpBean {



    /**
     * 本地文件路径
     */
    private String localPath;

    /**
     * FTP服务器保存目录,如果是根目录则为“/”
     */
    private String remotePath = "/";

    public FtpDirectoryBean(BaseFtpBean baseFtpBean) {
        super(baseFtpBean);
    }

    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    public String getRemotePath() {
        return remotePath;
    }

    public void setRemotePath(String remotePath) {
        this.remotePath = remotePath;
    }

    public void checkListParam() {
        super.checkParam();
        if (remotePath == null) {
            throw new RuntimeException("remotePath not found.");
        }
    }

    public void checkDownloadParam() {
        super.checkParam();
        if (remotePath == null) {
            throw new RuntimeException("remotePath not found.");
        }
    }

    public void checkDeleteParam() {
        super.checkParam();
        if (remotePath == null) {
            throw new RuntimeException("remotePath not found.");
        }
    }
}
