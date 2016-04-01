package com.voyageone.components.ftp.tools;

import com.jcraft.jsch.*;
import com.voyageone.common.util.StringUtils;
import com.voyageone.components.ftp.bean.BaseFtpBean;
import com.voyageone.components.ftp.bean.FtpDirectoryBean;
import com.voyageone.components.ftp.bean.FtpSubFileBean;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

public class SFtpUtil {

    private final static Logger logger = LoggerFactory.getLogger(SFtpUtil.class);

    /**
     * 连接SFTP服务器
     *
     * @param ftpBean FtpFilesBean
     * @return ChannelSftp ChannelSftp
     */
    public ChannelSftp linkFtp(BaseFtpBean ftpBean) {
        logger.info(String.format("sftp 连接开始: hostName[%s],prot[%s]", ftpBean.getHostname(), ftpBean.getPort()));
        try {
            JSch jsch = new JSch();
            int port = 22;
            if (ftpBean.getPort() != 0) {
                port = ftpBean.getPort();
            }
            jsch.getSession(ftpBean.getUsername(), ftpBean.getHostname(), port);
            Session sshSession = jsch.getSession(ftpBean.getUsername(), ftpBean.getHostname(), port);
            sshSession.setPassword(ftpBean.getPassword());
            Properties sshConfig = new Properties();
            sshConfig.put("StrictHostKeyChecking", "no");
            sshSession.setConfig(sshConfig);
            sshSession.setTimeout(ftpBean.getTimeout());
            sshSession.connect();
            Channel channel = sshSession.openChannel("sftp");
            channel.connect();
            ChannelSftp ftpClient = (ChannelSftp) channel;
            logger.info(ftpBean.getHostname() + " sftp 连接成功");
            return ftpClient;
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * 向FTP服务器上传文件
     *
     * @param fileBean  FtpSubFileBean
     * @param ftpClient ChannelSftp
     */
    public void uploadFile(FtpSubFileBean fileBean, ChannelSftp ftpClient) {
        logger.info(String.format("sftp 上传文件开始:localFile[%s%s],remoteFile[%s%s]", fileBean.getLocalPath(), fileBean.getLocalFilename(), fileBean.getRemotePath(), fileBean.getRemoteFilename()));

        InputStream fileInputStream;
        try {
            if (fileBean.getLocalFileStream() != null) {
                logger.info("localFileStream size=" + fileBean.getLocalFileStream().available());
                fileInputStream = fileBean.getLocalFileStream();
            } else {
                String localFile = BaseFtpUtils.getLocalFilePathName(fileBean);
                File file = new File(localFile);
                if (file.exists() && file.isFile()) {
                    fileInputStream = new FileInputStream(file);
                } else {
                    throw new RuntimeException("localFile not found.");
                }
            }
            if (fileInputStream != null) {
                String uploadPath = BaseFtpUtils.seperator;
                if (!StringUtils.isEmpty(fileBean.getRemotePath())) {
                    uploadPath = FilenameUtils.separatorsToUnix(fileBean.getRemotePath());
                    if (!uploadPath.startsWith(BaseFtpUtils.seperator)) {
                        uploadPath = BaseFtpUtils.seperator + uploadPath;
                    }
                }
                if (!StringUtils.isEmpty(uploadPath)) {
                    createDir(uploadPath, ftpClient);
                }
                ftpClient.put(fileInputStream, fileBean.getRemoteFilename());
            }
        } catch (RuntimeException e) {
            logger.error(fileBean.getRemoteFilename() + " sFtp 上传文件失败", e);
            throw e;
        } catch (Exception e) {
            logger.error(fileBean.getRemoteFilename() + " sFtp 上传文件失败", e);
            throw new RuntimeException(e);
        }
        logger.info(fileBean.getRemoteFilename() + " sFtp 上传文件结束");
    }

    /**
     * create Directory
     */
    private void createDir(String filepath, ChannelSftp sftp) throws SftpException {
        try {
            sftp.cd(filepath);
        } catch (SftpException e1) {
            String[] folders = filepath.split("/");
            for (String folder : folders) {
                if (!StringUtils.isEmpty(folder)) {
                    try {
                        sftp.cd(folder);
                    } catch (SftpException e) {
                        sftp.mkdir(folder);
                        sftp.cd(folder);
                    }
                }
            }
        }
    }

    /**
     * 断开FTP服务器
     *
     * @param ftpClient ChannelSftp
     */
    public void disconnectFtp(ChannelSftp ftpClient) {
        if (ftpClient == null) {
            return;
        }
        if (ftpClient.isConnected()) {
            ftpClient.disconnect();
        }
        logger.info("sFtp 断开结束");
    }

    /**
     * 下载文件
     *
     * @param fileBean  下载的文件
     * @param ftpClient ftpClient
     */
    public void downloadFile(FtpSubFileBean fileBean, ChannelSftp ftpClient) {
        try {
            File file = new File(BaseFtpUtils.getLocalFilePathName(fileBean));
            OutputStream fileOutputStream = new FileOutputStream(file);
            String remotePath = fileBean.getRemotePath();
            if (StringUtils.isEmpty(remotePath)) {
                remotePath = "/";
            }
            ftpClient.cd(remotePath);
            ftpClient.get(fileBean.getRemoteFilename(), fileOutputStream);
        } catch (RuntimeException e) {
            logger.error(fileBean.getRemoteFilename() + " sFtp 下载文件失败", e);
            throw e;
        } catch (Exception e) {
            logger.error(fileBean.getRemoteFilename() + " sFtp 下载文件失败", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 列出目录下的文件
     *
     * @param directoryBean directoryBean
     * @return list 文件名列表
     */
    public List<String> listFiles(FtpDirectoryBean directoryBean, ChannelSftp ftpClient) {
        List<String> fileNameList = new ArrayList<>();
        Vector fileList;
        try {
            fileList = ftpClient.ls(directoryBean.getRemotePath());
        } catch (SftpException e) {
            logger.error(directoryBean.getRemotePath() + " sFtp 列出目录下的文件", e);
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
     * 删除文件
     *
     * @param fileBean  要删除文件
     * @param ftpClient ChannelSftp
     */
    public void delete(FtpSubFileBean fileBean, ChannelSftp ftpClient) {
        logger.info(fileBean.getRemotePath() + "/" + fileBean.getRemoteFilename() + " 删除");
        try {
            ftpClient.cd(fileBean.getRemotePath());
            Vector fileNames = ftpClient.ls(fileBean.getRemoteFilename());
            if (fileNames.size() == 1) {
                ftpClient.rm(fileBean.getRemoteFilename());
            }
        } catch (SftpException e) {
            if (e.id == 2) {
                logger.error(fileBean.getRemoteFilename() + " sFtp 要删除文件not found");
                return;
            }
            logger.error(fileBean.getRemoteFilename() + " sFtp 要删除文件", e);
            throw new RuntimeException(e);
        }
    }

//    /**
//     * 将FTP服务器上文件移动位置
//     *
//     * @param ftpBean   FtpFilesBean
//     * @param ftpClient ChannelSftp
//     * @param fileName  文件名
//     */
//    public void removeOneFile(FtpFilesBean ftpBean, ChannelSftp ftpClient, String fileName) throws SftpException {
//        String bakPath = ftpBean.getRemote_bak_path();
//        String bakFileName = fileName.substring(0, fileName.lastIndexOf(".")) + "_" + DateTimeUtil.getNow(DateTimeUtil.DATE_TIME_FORMAT_2) + fileName.substring(fileName.lastIndexOf("."));
//        String srcFilePathName = ftpBean.getDown_remotepath() + "/" + fileName;
//        String tgtFilePathName = bakPath + "/" + bakFileName;
//        createDir(bakPath, ftpClient);
//        try {
//            logger.info("移动 " + srcFilePathName + " 到 " + tgtFilePathName + " 开始。");
//            if (StringUtils.isEmpty(fileName)) {
//                logger.error("fileName 不能为空或null");
//                return;
//            }
//            ftpClient.rename(srcFilePathName, tgtFilePathName);
//            logger.info("移动 " + srcFilePathName + " 到 " + tgtFilePathName + " 结束。");
//        } catch (Exception e) {
//            throw new RuntimeException("移动文件" + srcFilePathName + " 到 " + tgtFilePathName + "失败！");
//        }
//    }

}
