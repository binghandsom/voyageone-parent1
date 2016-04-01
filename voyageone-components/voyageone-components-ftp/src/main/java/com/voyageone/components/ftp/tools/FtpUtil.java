package com.voyageone.components.ftp.tools;

import com.voyageone.common.util.StringUtils;
import com.voyageone.components.ftp.bean.BaseFtpBean;
import com.voyageone.components.ftp.bean.FtpDirectoryBean;
import com.voyageone.components.ftp.bean.FtpSubFileBean;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FtpUtil {

    private final static Logger logger = LoggerFactory.getLogger(FtpUtil.class);

    private final String encoding = System.getProperty("file.encoding");

    private final String basCoding = "iso-8859-1";

    /**
     * 连接FTP服务器
     *
     * @param ftpBean FtpFilesBean
     * @return FTPClient FTPClient
     */
    public FTPClient linkFtp(BaseFtpBean ftpBean) {
        FTPClient ftpClient = new FTPClient();
        logger.info(String.format("ftp 连接开始: hostName[%s],prot[%s]", ftpBean.getHostname(), ftpBean.getPort()));
        try {
            ftpClient.setControlEncoding(encoding);
            ftpClient.setConnectTimeout(ftpBean.getTimeout());
            ftpClient.setDataTimeout(ftpBean.getTimeout());
            //连接FTP服务器
            if (ftpBean.getPort() == 0) {
                // 如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器
                ftpClient.connect(ftpBean.getHostname());
            } else {
                ftpClient.connect(ftpBean.getHostname(), ftpBean.getPort());
            }
            // 登录
            ftpClient.login(ftpBean.getUsername(), ftpBean.getPassword());
            // 设置文件传输类型为二进制
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            // 获取ftp登录应答代码
            int reply = ftpClient.getReplyCode();
            // 验证是否登陆成功
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftpClient.disconnect();
                throw new RuntimeException("FTP server refused connection.");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        logger.info(ftpBean.getHostname() + " Ftp 连接结束");
        return ftpClient;
    }


    /**
     * 断开FTP服务器
     *
     * @param ftpClient FTPClient
     */
    public void disconnectFtp(FTPClient ftpClient) {
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
     * 向FTP服务器上传文件
     */
    public void uploadFile(FtpSubFileBean fileBean, String basCoding, FTPClient ftpClient) {
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
                    createDir(uploadPath, basCoding, ftpClient);
                }
                ftpClient.storeFile(fileBean.getRemoteFilename(), fileInputStream);
            }
        } catch (RuntimeException e) {
            logger.error(fileBean.getRemoteFilename() + " Ftp 上传文件失败", e);
            throw e;
        } catch (Exception e) {
            logger.error(fileBean.getRemoteFilename() + " Ftp 上传文件失败", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * create Directory
     */
    private void createDir(String filepath, String basCoding, FTPClient ftpClient) throws IOException {
        if (!changeFolder(filepath, basCoding, ftpClient)) {
            String[] folders = filepath.split("/");
            for (String folder : folders) {
                if (!StringUtils.isEmpty(folder)) {
                    if (!changeFolder(filepath, basCoding, ftpClient)) {
                        ftpClient.makeDirectory(folder);
                        ftpClient.changeWorkingDirectory(folder);
                    }
                }
            }
        }
    }

    /**
     * 下载文件
     *
     * @param fileBean  下载的文件
     * @param ftpClient ftpClient
     */
    public void downloadFile(FtpSubFileBean fileBean, FTPClient ftpClient) {
        try {
            File file = new File(BaseFtpUtils.getLocalFilePathName(fileBean));
            OutputStream fileOutputStream = new FileOutputStream(file);
            String remoteFileName = BaseFtpUtils.getRemoteFilePathName(fileBean);
            String[] remoteFileNameArr = ftpClient.listNames(remoteFileName);
            if (remoteFileNameArr != null && remoteFileNameArr.length>0) {
                ftpClient.retrieveFile(remoteFileName, fileOutputStream);
            }
        } catch (RuntimeException e) {
            logger.error(fileBean.getRemoteFilename() + " Ftp 下载文件失败", e);
            throw e;
        } catch (Exception e) {
            logger.error(fileBean.getRemoteFilename() + " Ftp 下载文件失败", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 将FTP服务器上文件删除
     *
     * @param ftpBean   FtpBean
     * @param ftpClient FTPClient
     */
    public void delFile(FtpSubFileBean ftpBean, FTPClient ftpClient) {
        String remoteFileName = BaseFtpUtils.getRemoteFilePathName(ftpBean);
        try {
            String[] remoteFileNameArr = ftpClient.listNames(remoteFileName);
            if (remoteFileNameArr != null && remoteFileNameArr.length>0) {
                if (!ftpClient.deleteFile(remoteFileName)) {
                    throw new RuntimeException(String.format(" 删除Ftp下载文件失败[%s]", remoteFileName));
                }
            }
        } catch (IOException e) {
            logger.error(String.format(" 删除Ftp下载文件失败[%s]", remoteFileName), e);
            throw new RuntimeException(e);
        }
    }
//
//    /**
//     * 将FTP服务器上文件移动位置
//     * @param ftpBean           FtpBean
//     * @param ftpClient         FTPClient
//     * @param fileName         文件名
//     */
//    public void removeOneFile(FtpBean ftpBean, FTPClient ftpClient, String fileName) throws IOException {
//        boolean rmvSuccess;
//        String bakPath = ftpBean.getRemote_bak_path();
//        if(ftpClient.makeDirectory(bakPath)){
//            logger.info("创建目录 " + bakPath + " 成功！");
//        }else {
//            logger.info(bakPath + "目录已经存在无需创建！");
//        }
//        String bakFileName = fileName.substring(0, fileName.lastIndexOf(".")) + "_" + DateTimeUtil.getNow(DateTimeUtil.DATE_TIME_FORMAT_2) + fileName.substring(fileName.lastIndexOf("."));
//        String srcFilePathName = ftpBean.getDown_remotepath() + "/" +  fileName;
//        String tgtFilePathName = bakPath + "/" + bakFileName;
//        if(StringUtils.isEmpty(fileName)) return;
//        logger.info("移动 " + srcFilePathName  + " 到 " + tgtFilePathName + " 开始。");
//        rmvSuccess = ftpClient.rename(srcFilePathName, tgtFilePathName);
//        if (!rmvSuccess) {
//            logger.info("移动文件失败！");
//            throw new RuntimeException(fileName + "文件移动失败！");
//        }else{
//            logger.info("移动文件成功！");
//        }
//        logger.info("移动 " + srcFilePathName  + " 到 " + tgtFilePathName + " 结束。");
//    }

//    /**
//     * 读取目标目录夹下所有文件名, 过滤掉文件夹
//     * @param ftpBean           FtpBean
//     * @param ftpClient         FTPClient
//     * @param folderPath        文件路径
//     */
//    public List<String> getFolderFileNames(FtpBean ftpBean, FTPClient ftpClient, String folderPath)throws IOException {
//        logger.info(folderPath  + " 读取目标目录夹下所有文件名开始");
//        List<String> fileNames = new ArrayList<>();
//        //定位FTP目录夹
//        changeFolder(ftpBean, ftpClient);
//        // 获取文件列表
//        FTPFile[] fs = ftpClient.listFiles();
//        for (FTPFile ff : fs) {
//            if(ff.isFile()) {
//                logger.info("fileName=" + ff.getName());
//                fileNames.add(ff.getName());
//            }else{
//                logger.info("fileName=" + ff.getName() + "不是文件！");
//            }
//        }
//        logger.info(folderPath  + " 读取目标目录夹下所有文件名结束");
//        return fileNames;
//    }

    /**
     * 列出目录下的文件
     *
     * @param directoryBean directoryBean
     * @return list 文件名列表
     */
    public List<String> listFiles(FtpDirectoryBean directoryBean, FTPClient ftpClient) {
        List<String> fileNameList = new ArrayList<>();
        FTPFile[] fs;
        try {
            if (!changeFolder(directoryBean.getRemotePath(), directoryBean.getCoding(), ftpClient)) {
                throw new RuntimeException(String.format("ftpClient.changeWorkingDirectory error[%s]", directoryBean.getRemotePath()));
            }
            // 获取文件列表
            fs = ftpClient.listFiles();
        } catch (IOException e) {
            logger.error(directoryBean.getRemotePath() + " sFtp 列出目录下的文件", e);
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
     * 定位FTP目录夹
     */
    private boolean changeFolder(String remotePath, String coding, FTPClient ftpClient) {
        // 转移到FTP服务器目录至指定的目录下
        // 文件编码格式没有指定的情况，转换为iso-8859-1格式
        try {
            if (StringUtils.isNullOrBlank2(coding)) {
                return ftpClient.changeWorkingDirectory(new String(remotePath.getBytes(encoding), basCoding));
            } else {
                // 文件编码格式有指定的情况，转换为指定格式
                return ftpClient.changeWorkingDirectory(new String(remotePath.getBytes(encoding), coding));
            }
        } catch (IOException e) {
            return false;
        }
    }
}
