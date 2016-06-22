package com.voyageone.components.ftp.service;

import com.jcraft.jsch.*;
import com.voyageone.common.util.StringUtils;
import com.voyageone.components.ftp.bean.FtpConnectBean;
import com.voyageone.components.ftp.bean.FtpFileBean;
import org.apache.commons.io.FilenameUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

/**
 * SftpComponent
 *
 * @author chuanyu.liang 2016/5/10.
 * @version 2.0.0
 * @since 2.0.0
 */
public class SftpComponent extends BaseFtpComponent {

    protected ChannelSftp ftpClient;

    public SftpComponent(FtpConnectBean ftpConnectBean) {
        super(ftpConnectBean);
    }

    /**
     * 使用sftp协议 连接FTP服务器
     */
    @Override
    public void openConnect() {
        //check
        ftpConnectBean.checkParam();

        logger.info(String.format("sftp 连接开始: hostName[%s],prot[%s]", ftpConnectBean.getHostname(), ftpConnectBean.getPort()));
        try {
            JSch jsch = new JSch();
            int port = 22;
            if (ftpConnectBean.getPort() != 0) {
                port = ftpConnectBean.getPort();
            }

            jsch.getSession(ftpConnectBean.getUsername(), ftpConnectBean.getHostname(), port);
            Session sshSession = jsch.getSession(ftpConnectBean.getUsername(), ftpConnectBean.getHostname(), port);
            sshSession.setPassword(ftpConnectBean.getPassword());
            Properties sshConfig = new Properties();
            sshConfig.put("StrictHostKeyChecking", "no");
            sshSession.setConfig(sshConfig);
            //sshSession.setTimeout(ftpConnectBean.getTimeout());
            sshSession.setTimeout(1000);

            sshSession.connect();
            Channel channel = sshSession.openChannel("sftp");
            channel.connect();

            ftpClient = (ChannelSftp) channel;
            logger.info(ftpConnectBean.getHostname() + " sftp 连接成功");
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
            ftpClient.disconnect();
        }
        logger.info("sftp 断开结束");
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

        logger.info(String.format("sftp 上传文件开始:localFile[%s%s],remoteFile[%s%s]",
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
                ftpClient.put(fileInputStream, fileBean.getRemoteFilename());
            }
        } catch (RuntimeException e) {
            logger.error(fileBean.getRemoteFilename() + " sftp 上传文件失败", e);
            throw e;
        } catch (Exception e) {
            logger.error(fileBean.getRemoteFilename() + " sftp 上传文件失败", e);
            throw new RuntimeException(e);
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException ignored) {
                }
            }
        }
        logger.info(fileBean.getRemoteFilename() + " sftp 上传文件结束");
    }

    /**
     * 使用sftp协议下载文件从远程服务器
     */
    @Override
    public void downloadFile(FtpFileBean fileBean) {
        //check
        fileBean.checkDownloadParam();
        //check connection
        if (!isConnection()) {
            openConnect();
        }

        logger.info(String.format("sftp 下载文件开始:localFile[%s%s],remoteFile[%s%s]",
                fileBean.getLocalPath(), fileBean.getLocalFilename(), fileBean.getRemotePath(), fileBean.getRemoteFilename()));

        try {
            String remotePath = fileBean.getRemotePath();
            if (StringUtils.isEmpty(remotePath)) {
                remotePath = "/";
            }
            ftpClient.cd(remotePath);

            File file = new File(getLocalFilePathName(fileBean));
            OutputStream fileOutputStream = new FileOutputStream(file);
            ftpClient.get(fileBean.getRemoteFilename(), fileOutputStream);
        } catch (RuntimeException e) {
            logger.error(fileBean.getRemoteFilename() + " sftp 下载文件失败", e);
            throw e;
        } catch (Exception e) {
            logger.error(fileBean.getRemoteFilename() + " sftp 下载文件失败", e);
            throw new RuntimeException(e);
        }

        logger.info(fileBean.getRemoteFilename() + " sftp 下载文件结束");
    }

    /**
     * 使用sftp协议删除文件从远程服务器
     */
    @Override
    public void deleteFile(FtpFileBean fileBean) {
        //check
        fileBean.checkDeleteParam();
        //check connection
        if (!isConnection()) {
            openConnect();
        }

        logger.info(fileBean.getRemotePath() + "/" + fileBean.getRemoteFilename() + " 删除");
        try {
            ftpClient.cd(fileBean.getRemotePath());
            Vector fileNames = ftpClient.ls(fileBean.getRemoteFilename());
            if (fileNames.size() == 1) {
                ftpClient.rm(fileBean.getRemoteFilename());
            }
        } catch (SftpException e) {
            if (e.id == 2) {
                logger.error(fileBean.getRemoteFilename() + " sftp 要删除文件not found");
                return;
            }
            logger.error(fileBean.getRemoteFilename() + " sftp 要删除文件", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 使用sftp协议 列出目录下的文件
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
        Vector fileList;
        try {
            fileList = ftpClient.ls(remotePath);
        } catch (SftpException e) {
            logger.error(remotePath + " sftp 列出目录下的文件", e);
            throw new RuntimeException(e);
        }

        for (Object aFileList : fileList) {
            ChannelSftp.LsEntry lsEntry = (ChannelSftp.LsEntry) aFileList;
            SftpATTRS subDirectory = lsEntry.getAttrs();
            String fileName = lsEntry.getFilename();
            if (".".equals(fileName) || "..".equals(fileName) || subDirectory.isDir()) {
                continue;
            }
            fileNameList.add(fileName);
        }
        return fileNameList;
    }

    /**
     * 使用sftp协议 定位FTP目录夹
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

        try {
            ftpClient.cd(remotePath);
            return true;
        } catch (SftpException e1) {
            return false;
        }
    }

    /**
     * 使用sftp协议 变更远程目录，如果目录不存在则创建目录
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
            ftpClient.cd(remotePath);
        } catch (SftpException e1) {
            String[] folders = remotePath.split("/");
            for (String folder : folders) {
                if (!StringUtils.isEmpty(folder)) {
                    try {
                        ftpClient.cd(folder);
                    } catch (SftpException e2) {
                        try {
                            ftpClient.mkdir(folder);
                            ftpClient.cd(folder);
                        } catch (SftpException e3) {
                            throw new RuntimeException(e3);
                        }
                    }
                }
            }
        }
    }

    /**
     * 使用sftp协议 检查FTP是否连接
     */
    @Override
    protected boolean isConnection() {
        //check connection
        return ftpClient != null && ftpClient.isConnected();
    }
}
