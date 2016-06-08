package com.voyageone.components.ftp.service;

import com.voyageone.common.util.StringUtils;
import com.voyageone.components.ftp.bean.FtpConnectBean;
import com.voyageone.components.ftp.bean.FtpFileBean;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * FtpComponent
 *
 * @author chuanyu.liang 2016/5/10.
 * @version 2.0.0
 * @since 2.0.0
 */
public class FtpComponent extends BaseFtpComponent {

    private final static String BASE_CODING = "iso-8859-1";

    private String encoding = System.getProperty("file.encoding");

    protected FTPClient ftpClient;

    public FtpComponent(FtpConnectBean ftpConnectBean) {
        super(ftpConnectBean);
    }

    /**
     * 使用ftp协议 连接FTP服务器
     */
    @Override
    public void openConnect() {
        //check
        ftpConnectBean.checkParam();

        //open connect
        logger.info(String.format("ftp 连接开始: hostName[%s],prot[%s]", ftpConnectBean.getHostname(), ftpConnectBean.getPort()));
        ftpClient = new FTPClient();
        try {
            if (!StringUtils.isEmpty(ftpConnectBean.getCoding())) {
                ftpClient.setControlEncoding(ftpConnectBean.getCoding());
            }
            ftpClient.setConnectTimeout(ftpConnectBean.getTimeout());
            ftpClient.setDataTimeout(ftpConnectBean.getTimeout());
            //连接FTP服务器
            if (ftpConnectBean.getPort() == 0) {
                // 如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器
                ftpClient.connect(ftpConnectBean.getHostname());
            } else {
                ftpClient.connect(ftpConnectBean.getHostname(), ftpConnectBean.getPort());
            }
            // 登录
            ftpClient.login(ftpConnectBean.getUsername(), ftpConnectBean.getPassword());
            // 设置文件传输类型为二进制
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            // 获取ftp登录应答代码
            int reply = ftpClient.getReplyCode();
            // 验证是否登陆成功
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftpClient.disconnect();
                throw new RuntimeException("ftp server refused connection.");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        logger.info(ftpConnectBean.getHostname() + " ftp 连接结束");
    }

    /**
     * 使用ftp协议 断开FTP服务器
     */
    @Override
    public void closeConnect() {
        if (ftpClient == null) {
            return;
        }
        if (ftpClient.isConnected()) {
            try {
                ftpClient.disconnect();
            } catch (IOException ignored) {
            }
        }
        logger.info("Ftp 断开结束");
    }

    /**
     * 使用ftp协议保存文件到远程服务器
     */
    @Override
    public void uploadFile(FtpFileBean fileBean) {
        //check
        fileBean.checkUploadParam();
        //check connection
        if (!isConnection()) {
            openConnect();
        }

        logger.info(String.format("ftp 上传文件开始:localFile[%s%s],remoteFile[%s%s]",
                fileBean.getLocalPath(), fileBean.getLocalFilename(), fileBean.getRemotePath(), fileBean.getRemoteFilename()));

        InputStream fileInputStream = null;
        try {
            if (fileBean.getLocalFileStream() != null) {
                logger.info("localFileStream size=" + fileBean.getLocalFileStream().available());
                fileInputStream = fileBean.getLocalFileStream();
            } else {
                String localFile = getLocalFilePathName(fileBean);
                File file = new File(localFile);
                if (file.exists() && file.isFile()) {
                    fileInputStream = new FileInputStream(file);
                } else {
                    throw new RuntimeException("localFile not found.");
                }
            }
            if (fileInputStream != null) {
                String uploadPath = seperator;
                if (!StringUtils.isEmpty(fileBean.getRemotePath())) {
                    uploadPath = FilenameUtils.separatorsToUnix(fileBean.getRemotePath());
                    if (!uploadPath.startsWith(seperator)) {
                        uploadPath = seperator + uploadPath;
                    }
                }
                if (!StringUtils.isEmpty(uploadPath)) {
                    changeOrCreatePath(uploadPath);
                }
                ftpClient.storeFile(fileBean.getRemoteFilename(), fileInputStream);
            }
        } catch (RuntimeException e) {
            logger.error(fileBean.getRemoteFilename() + " ftp 上传文件失败", e);
            throw e;
        } catch (Exception e) {
            logger.error(fileBean.getRemoteFilename() + " ftp 上传文件失败", e);
            throw new RuntimeException(e);
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException ignored) {
                }
            }
        }
    }

    /**
     * 使用ftp协议下载文件从远程服务器
     */
    @Override
    public void downloadFile(FtpFileBean fileBean) {
        //check
        fileBean.checkDownloadParam();
        //check connection
        if (!isConnection()) {
            openConnect();
        }

        logger.info(String.format("ftp 下载文件开始:localFile[%s%s],remoteFile[%s%s]",
                fileBean.getLocalPath(), fileBean.getLocalFilename(), fileBean.getRemotePath(), fileBean.getRemoteFilename()));

        try {
            File file = new File(getLocalFilePathName(fileBean));
            OutputStream fileOutputStream = new FileOutputStream(file);
            String remoteFileName = getRemoteFilePathName(fileBean);
            ftpClient.retrieveFile(remoteFileName, fileOutputStream);
        } catch (RuntimeException e) {
            logger.error(fileBean.getRemoteFilename() + " ftp 下载文件失败", e);
            throw e;
        } catch (Exception e) {
            logger.error(fileBean.getRemoteFilename() + " ftp 下载文件失败", e);
            throw new RuntimeException(e);
        }

        logger.info(fileBean.getRemoteFilename() + " ftp 下载文件结束");
    }

    /**
     * 使用ftp协议删除文件从远程服务器
     */
    @Override
    public void deleteFile(FtpFileBean fileBean) {
        //check
        fileBean.checkDeleteParam();
        //check connection
        if (!isConnection()) {
            openConnect();
        }

        String remoteFileName = getRemoteFilePathName(fileBean);
        try {
            String[] remoteFileNameArr = ftpClient.listNames(remoteFileName);
            if (remoteFileNameArr != null && remoteFileNameArr.length > 0) {
                if (!ftpClient.deleteFile(remoteFileName)) {
                    throw new RuntimeException(String.format(" 删除ftp下载文件失败[%s]", remoteFileName));
                }
            }
        } catch (IOException e) {
            logger.error(String.format(" 删除ftp下载文件失败[%s]", remoteFileName), e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 使用ftp协议 列出目录下的文件
     */
    @Override
    public List<String> listFilePath(String remotePath) {
        //check
        if (StringUtils.isEmpty(remotePath)) {
            throw new RuntimeException("remotePath not found.");
        }
        //check connection
        if (!isConnection()) {
            openConnect();
        }

        List<String> fileNameList = new ArrayList<>();
        FTPFile[] fs;
        try {
            if (!changePath(remotePath)) {
                throw new RuntimeException(String.format("ftpClient.changeWorkingDirectory error[%s]", remotePath));
            }
            // 获取文件列表
            fs = ftpClient.listFiles();
        } catch (IOException e) {
            logger.error(remotePath + " ftp 列出目录下的文件", e);
            throw new RuntimeException(e);
        }

        for (FTPFile ff : fs) {
            if (ff.isFile()) {
                fileNameList.add(ff.getName());
            }
        }
        return fileNameList;
    }

    /**
     * 使用ftp协议 定位FTP目录夹
     */
    @Override
    public boolean changePath(String remotePath) {
        //check
        if (StringUtils.isEmpty(remotePath)) {
            throw new RuntimeException("remotePath not found.");
        }
        //check connection
        if (!isConnection()) {
            openConnect();
        }

        // 转移到FTP服务器目录至指定的目录下
        // 文件编码格式没有指定的情况，转换为iso-8859-1格式
        try {
            if (StringUtils.isNullOrBlank2(ftpConnectBean.getCoding())) {
                return ftpClient.changeWorkingDirectory(new String(remotePath.getBytes(encoding), BASE_CODING));
            } else {
                // 文件编码格式有指定的情况，转换为指定格式
                return ftpClient.changeWorkingDirectory(new String(remotePath.getBytes(encoding), ftpConnectBean.getCoding()));
            }
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * 使用ftp协议 变更远程目录，如果目录不存在则创建目录
     */
    @Override
    public void changeOrCreatePath(String remotePath) {
        //check
        if (StringUtils.isEmpty(remotePath)) {
            throw new RuntimeException("remotePath not found.");
        }
        //check connection
        if (!isConnection()) {
            openConnect();
        }

        try {
            if (!changePath(remotePath)) {
                String[] folders = remotePath.split("/");
                for (String folder : folders) {
                    if (!StringUtils.isEmpty(folder)) {
                        if (!changePath(remotePath)) {
                            ftpClient.makeDirectory(folder);
                            ftpClient.changeWorkingDirectory(folder);
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 使用ftp协议 检查FTP是否连接
     */
    @Override
    protected boolean isConnection() {
        //check connection
        return ftpClient != null && ftpClient.isConnected();
    }

    /**
     * 使用ftp协议 设置被动模式传输
     */
    @Override
    public void enterLocalPassiveMode() {
        //check connection
        if (!isConnection()) {
            openConnect();
        }
        ftpClient.enterLocalPassiveMode();
    }
}
