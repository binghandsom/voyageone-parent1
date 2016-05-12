package com.voyageone.components.base;

import com.jcraft.jsch.ChannelSftp;
import com.voyageone.common.configs.beans.FtpBean;
import com.voyageone.common.util.SFtpUtil;
import com.voyageone.components.FileTransferProtocol;
import com.voyageone.components.service.SFtpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.io.InputStream;

/**
 * @author aooer 2016/5/10.
 * @version 2.0.0
 * @since 2.0.0
 */
public class SFtpBase extends FileTransferProtocol {

    private static final Logger log = LoggerFactory.getLogger(SFtpService.class);

    @Override
    protected boolean storeFile(String url, String port,String userName, String password, String fileName, String destFolder, InputStream uploadInputStream, String encoding, int timeOut) {
        boolean result = false;
        ChannelSftp ftpClient = null;
        SFtpUtil ftpUtil = null;
        try {
            ftpUtil = new SFtpUtil();
            FtpBean ftpBean = buildStoreBean(url, port, userName, password, fileName, destFolder, uploadInputStream, encoding);
            ftpClient = ftpUtil.linkFtp(ftpBean);
            Assert.notNull(ftpClient);
            boolean isSuccess = ftpUtil.uploadFile(ftpBean, ftpClient);
            if (!isSuccess) throw new Exception("upload error");
            result = true;
        } catch (Exception e) {
            log.error("保存文件到远程服务器出现异常", e);
        } finally {
            if (ftpClient != null) {
                try {
                    ftpUtil.disconnectFtp(ftpClient);
                } catch (Exception e) {
                    log.error("关闭远程服务器SFTP连接出现异常", e);
                }
            }
        }
        return result;
    }

    @Override
    protected int downloadFile(String url, String port,String userName, String password, String fileName, String localPath, String remotePath, String encoding) {
        Assert.notNull(null, "此方法封装暂定");
        return 0;
    }
}
