package com.voyageone.components.ftp.service;

import com.voyageone.components.ComponentBase;
import com.voyageone.components.ftp.bean.BaseFtpBean;
import com.voyageone.components.ftp.bean.FtpDirectoryBean;
import com.voyageone.components.ftp.bean.FtpFilesBean;
import com.voyageone.components.ftp.bean.FtpSubFileBean;
import com.voyageone.components.ftp.tools.FtpUtil;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author chuanyu.liang 2016/5/10.
 * @version 2.0.0
 * @since 2.0.0
 */
@Service
@EnableRetry
public class FtpService extends ComponentBase {

    /**
     * 使用ftp协议 连接FTP服务器
     */
    public FTPClient linkFtp(BaseFtpBean ftpBean) {
        //check
        ftpBean.checkParam();
        FtpUtil ftpUtil = new FtpUtil();
        return ftpUtil.linkFtp(ftpBean);
    }

    /**
     * 使用ftp协议 断开FTP服务器
     */
    public void disconnectFtp(FTPClient ftpClient) {
        FtpUtil ftpUtil = new FtpUtil();
        ftpUtil.disconnectFtp(ftpClient);
    }


    /**
     * 使用ftp协议保存文件到远程服务器
     */
    public void uploadFile(FtpSubFileBean fileBean, BaseFtpBean ftpBean, FTPClient ftpClient) {
        //check
        fileBean.checkUploadParam();

        FtpUtil ftpUtil = new FtpUtil();

        try {
            ftpUtil.uploadFile(fileBean, ftpBean.getCoding(), ftpClient);
        } finally {
            if (fileBean.getLocalFileStream() != null) {
                try {
                    fileBean.getLocalFileStream().close();
                } catch (IOException ignored) {
                }
            }
        }
    }

    /**
     * 使用ftp协议保存文件到远程服务器
     */
    public void downloadFile(FtpSubFileBean fileBean, BaseFtpBean ftpBean, FTPClient ftpClient) {
        //check
        fileBean.checkDownloadParam();

        FtpUtil ftpUtil = new FtpUtil();
        ftpUtil.downloadFile(fileBean, ftpClient);
    }

    /**
     * 使用ftp协议 列出目录下的文件
     */
    @Retryable
    public List<String> listFiles(FtpDirectoryBean ftpBean) {
        //check
        ftpBean.checkListParam();

        List<String> result = new ArrayList<>();
        FtpUtil ftpUtil = new FtpUtil();
        FTPClient ftpClient = null;
        try {
            ftpClient = ftpUtil.linkFtp(ftpBean);
            List<String> fileNames = ftpUtil.listFiles(ftpBean, ftpClient);
            result.addAll(fileNames);
        } finally {
            if (ftpClient != null) {
                ftpUtil.disconnectFtp(ftpClient);
            }
        }

        return result;
    }

    /**
     * 使用ftp协议保存文件到远程服务器
     */
    public void uploadFiles(FtpFilesBean ftpBean) {
        //check
        ftpBean.checkUploadParam();

        FtpUtil ftpUtil = new FtpUtil();

        int maxRetry = 3;
        Exception ex1 = null;
        for (int i = 0; i < maxRetry; i++) {
            FTPClient ftpClient = null;
            try {
                ftpClient = ftpUtil.linkFtp(ftpBean);
                for (FtpSubFileBean fileBean : ftpBean.getFileBeans()) {
                    ftpUtil.uploadFile(fileBean, ftpBean.getCoding(), ftpClient);
                }
                break;
            } catch (Exception ex) {
                ex1 = ex;
            } finally {
                if (ftpClient != null) {
                    ftpUtil.disconnectFtp(ftpClient);
                }
            }
        }
        if (ex1 != null) {
            logger.error("ftp uploadFiles:=", ex1);
        }

        for (FtpSubFileBean fileBean : ftpBean.getFileBeans()) {
            if (fileBean.getLocalFileStream() != null) {
                try {
                    fileBean.getLocalFileStream().close();
                } catch (IOException ignored) {
                }
            }
        }
    }

    /**
     * 使用ftp协议下载文件从远程服务器
     */
    @Retryable
    public void downloadFiles(FtpFilesBean ftpBean) {
        //check
        ftpBean.checkDownloadParam();

        FtpUtil ftpUtil = new FtpUtil();

        FTPClient ftpClient = null;
        try {
            ftpClient = ftpUtil.linkFtp(ftpBean);
            for (FtpSubFileBean fileBean : ftpBean.getFileBeans()) {
                ftpUtil.downloadFile(fileBean, ftpClient);
            }
        } finally {
            if (ftpClient != null) {
                ftpUtil.disconnectFtp(ftpClient);
            }
        }
    }

    /**
     * 使用ftp协议下载文件从远程服务器
     */
    @Retryable
    public void downloadDirector(FtpDirectoryBean ftpBean) {
        List<String> fileNames = listFiles(ftpBean);
        FtpFilesBean downloadFtpBean = new FtpFilesBean(ftpBean);
        for (String fileName : fileNames) {
            downloadFtpBean.addFileBean(new FtpSubFileBean(ftpBean.getLocalPath(), fileName, ftpBean.getRemotePath(), fileName));
        }
        downloadFiles(downloadFtpBean);
    }

    /**
     * 使用ftp协议下载文件从远程服务器
     */
    @Retryable
    public void deleteFiles(FtpFilesBean ftpBean) {
        //check
        ftpBean.checkDeleteParam();

        FtpUtil ftpUtil = new FtpUtil();

        FTPClient ftpClient = null;
        try {
            ftpClient = ftpUtil.linkFtp(ftpBean);
            for (FtpSubFileBean fileBean : ftpBean.getFileBeans()) {
                ftpUtil.delFile(fileBean, ftpClient);
            }
        } finally {
            if (ftpClient != null) {
                ftpUtil.disconnectFtp(ftpClient);
            }
        }
    }
}
