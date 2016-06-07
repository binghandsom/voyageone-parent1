package com.voyageone.components.ftp.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.InputStream;

/**
 * FtpFileBean
 *
 * @author chuanyu.liang 2016/5/10.
 * @version 2.0.0
 * @since 2.0.0
 */
public class FtpFileBean {
    /**
     * 本地文件路径
     */
    private String localPath;

    /**
     * 本地文件输入流(优先于localFilename)
     */
    @JsonIgnore
    private InputStream localFileStream;
    /**
     * 本地文件名
     */
    private String localFilename;

    /**
     * FTP服务器保存目录,如果是根目录则为“/”
     */
    private String remotePath = "/";

    /**
     * 上传到FTP服务器上的文件名(如果为空，则是localFilename)
     */
    private String remoteFilename;


    public FtpFileBean() {
    }

    public FtpFileBean(String localPath, String localFilename) {
        this.localPath = localPath;
        this.localFilename = localFilename;
    }

    public FtpFileBean(String localPath, String localFilename, String remotePath) {
        this.localPath = localPath;
        this.localFilename = localFilename;
        this.remotePath = remotePath;
        this.remoteFilename = localFilename;
    }

    public FtpFileBean(String localPath, String localFilename, String remotePath, String remoteFilename) {
        this.localPath = localPath;
        this.localFilename = localFilename;
        this.remotePath = remotePath;
        this.remoteFilename = remoteFilename;
    }

    public FtpFileBean(InputStream localFileStream, String remoteFilename) {
        this.localFileStream = localFileStream;
        this.remoteFilename = remoteFilename;
    }

    public FtpFileBean(InputStream localFileStream, String remotePath, String remoteFilename) {
        this.localFileStream = localFileStream;
        this.remotePath = remotePath;
        this.remoteFilename = remoteFilename;
    }

    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    public InputStream getLocalFileStream() {
        return localFileStream;
    }

    public void setLocalFileStream(InputStream localFileStream) {
        this.localFileStream = localFileStream;
    }

    public String getLocalFilename() {
        return localFilename;
    }

    public void setLocalFilename(String localFilename) {
        this.localFilename = localFilename;
    }

    public String getRemotePath() {
        return remotePath;
    }

    public void setRemotePath(String remotePath) {
        this.remotePath = remotePath;
    }

    public String getRemoteFilename() {
        return remoteFilename;
    }

    public void setRemoteFilename(String remoteFilename) {
        this.remoteFilename = remoteFilename;
    }

    public void checkUploadParam() {
        if (localFileStream == null && localFilename == null) {
            throw new RuntimeException("localFileName not found.");
        }

        if (localFileStream != null && remoteFilename == null) {
            throw new RuntimeException("localFileName not found.");
        }
    }

    public void checkDownloadParam() {
        if (localFilename == null) {
            throw new RuntimeException("localFileName not found.");
        }

        if (remoteFilename == null) {
            throw new RuntimeException("localFileName not found.");
        }
    }

    public void checkDeleteParam() {
        if (remotePath == null) {
            throw new RuntimeException("remotePath not found.");
        }

        if (remoteFilename == null) {
            throw new RuntimeException("remoteFilename not found.");
        }
    }
}
