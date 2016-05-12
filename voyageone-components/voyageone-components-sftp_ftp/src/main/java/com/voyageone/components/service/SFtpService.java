package com.voyageone.components.service;

import com.voyageone.components.base.SFtpBase;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.io.InputStream;

/**
 * @author aooer 2016/5/10.
 * @version 2.0.0
 * @since 2.0.0
 */
@Service
public class SFtpService extends SFtpBase {

    /**
     * 使用sftp协议保存文件到远程服务器
     *
     * @param url               host
     * @param port              port
     * @param userName          user
     * @param password          password
     * @param fileName          uplaod fileName
     * @param destFolder        upload folder
     * @param uploadInputStream InputStream
     * @param encoding          file encoding
     * @return ftpBean
     */
    @Retryable
    public boolean storeFile(String url, String port, String userName, String password, String fileName, String destFolder, InputStream uploadInputStream, String encoding) {
        return super.storeFile(url, port, userName, password, fileName, destFolder, uploadInputStream, encoding, 0);
    }

    /**
     * 使用sftp协议下载文件从远程服务器
     *
     * @param url        host
     * @param port       port
     * @param userName   user
     * @param password   password
     * @param fileName   uplaod fileName
     * @param localPath  localPath
     * @param remotePath remotePath
     * @param encoding   file encoding
     * @return ftpBean
     */
    @Retryable
    public int downloadFile(String url, String port, String userName, String password, String fileName, String localPath, String remotePath, String encoding) {
        return super.downloadFile(url, port, userName, password, fileName, localPath, remotePath, encoding);
    }
}
