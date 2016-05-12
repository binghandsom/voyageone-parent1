package com.voyageone.components.base;

import com.voyageone.common.configs.beans.FtpBean;
import com.voyageone.common.util.FtpUtil;
import com.voyageone.components.FileTransferProtocol;
import com.voyageone.components.service.FtpService;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.io.InputStream;

/**
 * @author aooer 2016/5/10.
 * @version 2.0.0
 * @since 2.0.0
 */
public class FtpBase extends FileTransferProtocol {

    private static final Logger log = LoggerFactory.getLogger(FtpService.class);

    @Override
    protected boolean storeFile(String url,String port, String userName, String password, String fileName, String destFolder, InputStream uploadInputStream, String encoding, int timeOut) {
        boolean result = false;
        FTPClient ftpClient = null;
        FtpUtil ftpUtil = null;
        try {
            FtpBean ftpBean = buildStoreBean(url, port, userName, password, fileName, destFolder, uploadInputStream, encoding);
            ftpUtil = new FtpUtil();
            ftpClient = ftpUtil.linkFtp(ftpBean);
            Assert.notNull(ftpClient);
            if (!ftpClient.changeWorkingDirectory(ftpBean.getUpload_path())) return false;
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            ftpClient.setConnectTimeout(timeOut);
            ftpClient.storeFile(fileName, uploadInputStream);
            result = true;
        } catch (Exception e) {
            log.error("保存文件到远程服务器出现异常", e);
        } finally {
            if (ftpClient != null) {
                try {
                    ftpUtil.disconnectFtp(ftpClient);
                } catch (Exception e) {
                    log.error("关闭远程服务器FTP连接出现异常", e);
                }
            }
        }
        return result;
    }

    @Override
    protected int downloadFile(String url,String port, String userName, String password, String fileName, String localPath, String remotePath, String encoding) {
        int result = 0;
        FTPClient ftpClient = null;
        FtpUtil ftpUtil = null;
        try {
            FtpBean ftpBean = buildDownloadBean(url, port, userName, password, fileName, localPath, remotePath, encoding);
            ftpUtil = new FtpUtil();
            ftpClient = ftpUtil.linkFtp(ftpBean);
            Assert.notNull(ftpClient);
            result = ftpUtil.downFile(ftpBean, ftpClient);
        } catch (Exception e) {
            log.error("从远程服务器下载文件出现异常", e);
        } finally {
            if (ftpClient != null) {
                try {
                    ftpUtil.disconnectFtp(ftpClient);
                } catch (Exception e) {
                    log.error("关闭远程服务器FTP连接出现异常", e);
                }
            }
        }
        return result;
    }
}
