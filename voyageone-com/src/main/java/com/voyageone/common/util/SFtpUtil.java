package com.voyageone.common.util;

import com.jcraft.jsch.*;
import com.voyageone.common.configs.beans.FtpBean;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;
import java.util.*;

/**
 * @author fred
 */
public class SFtpUtil {

    private final Log logger = LogFactory.getLog(getClass());
    private final String seperator = "/";

    /**
     * Description: 向FTP服务器上传文件
     *
     * @param ftpBean           FtpBean
     * @return ChannelSftp        ChannelSftp
     *   成功返回true，否则返回false
     */
    public  boolean uploadFile(FtpBean ftpBean,ChannelSftp ftpClient) throws IOException {
        logger.info(ftpBean.getDown_filename() + "  Ftp 上传文件开始");
        logger.info("hostname=" + ftpBean.getUrl() + "  port=" + ftpBean.getPort());
        logger.info("  uploadPath=" + ftpBean.getUpload_path());
        logger.info("  uploadLocalPath=" + ftpBean.getUpload_localpath());
        logger.info("  uploadFileName=" + ftpBean.getUpload_filename());
        boolean result = false;

        if(ftpBean.getUpload_filename() != null){
            String localFile = ftpBean.getUpload_localpath() + this.seperator + ftpBean.getUpload_filename();
            File file = new File(localFile);

            if(file.isFile()){
                String remoteFile = ftpBean.getUpload_path() + this.seperator + ftpBean.getUpload_filename();
                File rfile = new File(remoteFile);
                String rpath = rfile.getParent();
                try {
                    createDir(rpath, ftpClient);

                    ftpClient.put(new FileInputStream(file), file.getName());
                    result = true;
                    logger.info(ftpBean.getUpload_filename() + " Ftp 上传文件成功");
                }
                catch (Exception e) {
                    e.printStackTrace();
                    logger.info(ftpBean.getUpload_filename() + " Ftp 上传文件失败");
                }
            }
        }
        logger.info(ftpBean.getUpload_filename() + " Ftp 上传文件结束");

        return result;
    }

    /**
     * create Directory
     */
    private void createDir(String filepath, ChannelSftp sftp) throws SftpException {
        try {
            sftp.cd(filepath);
        } catch (SftpException e1) {
            sftp.mkdir(filepath);
        }
    }
    /**
     * Description: 连接FTP服务器
     *
     * @param ftpBean           FtpBean
     * @return ChannelSftp        ChannelSftp
     */
    public ChannelSftp linkFtp(FtpBean ftpBean) throws IOException, JSchException {
        ChannelSftp ftpClient = new ChannelSftp();
        logger.info(ftpBean.getUrl() + "  Ftp 连接开始");
        logger.info("hostname=" + ftpBean.getUrl() + "  port=" + ftpBean.getPort());
        //boolean result = false;
        try {
            JSch jsch = new JSch();
            int port = StringUtils.isNullOrBlank2(ftpBean.getPort())  || !StringUtils.isNumeric(ftpBean.getPort()) ? 22 : Integer.valueOf(ftpBean.getPort());
            jsch.getSession(ftpBean.getUsername(), ftpBean.getUrl(), port);
            Session sshSession = jsch.getSession(ftpBean.getUsername(), ftpBean.getUrl(), port);
            logger.info("Session created.");
            sshSession.setPassword(ftpBean.getPassword());
            Properties sshConfig = new Properties();
            sshConfig.put("StrictHostKeyChecking", "no");
            sshSession.setConfig(sshConfig);
            sshSession.connect();
            logger.info("Session connected.");
            logger.info("Opening Channel.");
            Channel channel = sshSession.openChannel("sftp");
            channel.connect();
            ftpClient = (ChannelSftp) channel;
            logger.info("Connected to " + ftpBean.getUrl() + ".");

        } catch (Exception e) {
            disconnectFtp(ftpClient);
            throw e;
        }
        logger.info(ftpBean.getUrl()  + " Ftp 连接成功");
        return ftpClient;
    }

    /**
     * Description: 断开FTP服务器
     *
     * @param ftpClient         ChannelSftp
     *  false: 断开出错 true : 断开成功
     */
    public void  disconnectFtp(ChannelSftp ftpClient) throws IOException {
        logger.info("Ftp 断开开始");
        if (ftpClient.isConnected()) {

            ftpClient.disconnect();
            logger.info("Ftp 断开成功");
        }else if(ftpClient.isClosed()){
            logger.info("Ftp 已经断开");
        }
        logger.info("Ftp 断开结束");

    }

    /**
     * Description: 从FTP服务器下载文件
     *
     * @param ftpBean           FtpBean
     * @return result            0: 执行出错
     *                            1: 没有找到指定文件
     *                            2: 执行成功
     */
    public int downFile(FtpBean ftpBean , ChannelSftp ftpClient) throws Exception {
        logger.info(ftpBean.getDown_filename() + "  Ftp 下载文件开始");

        logger.info("hostname=" + ftpBean.getUrl() + "  port=" + ftpBean.getPort());
        logger.info("  remotePath=" + ftpBean.getDown_remotepath());
        logger.info("  localPath=" + ftpBean.getDown_localpath());
        logger.info("  fileName=" + ftpBean.getDown_filename());
        int result = 0;

        try {
            //下载文件为空，下载全部文件
            if (StringUtils.isNullOrBlank2(ftpBean.getDown_filename())) {
                downloadByDirectory(ftpBean.getDown_remotepath(), ftpBean.getDown_localpath(), ftpClient);
                result = 2;
            }
            else {
                download(ftpBean.getDown_remotepath(), ftpBean.getDown_filename(), ftpBean.getDown_localpath(), ftpClient);
                result = 2;
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

        logger.info(ftpBean.getDown_filename()  + " Ftp 下载文件结束");
        return result;
    }

    /**
     * 下载单个文件
     *
     * @param directory
     *            下载目录
     * @param downloadFile
     *            下载的文件
     * @param saveDirectory
     *            存在本地的路径
     *
     * @throws Exception
     */
    public void download(String directory, String downloadFile, String saveDirectory, ChannelSftp ftpClient) throws Exception {
        String saveFile = saveDirectory + this.seperator + downloadFile;

        ftpClient.cd(directory);
        File file = new File(saveFile);
        ftpClient.get(downloadFile, new FileOutputStream(file));

    }

    /**
     * 下载目录下全部文件
     *
     * @param directory
     *            下载目录
     *
     * @param saveDirectory
     *            存在本地的路径
     *
     * @throws Exception
     */
    public void downloadByDirectory(String directory, String saveDirectory, ChannelSftp ftpClient) throws Exception {
        String downloadFile = "";
        List<String> downloadFileList = listFiles(directory, ftpClient);

        for (String aDownloadFileList : downloadFileList) {
            if (!aDownloadFileList.contains(".")) {
                continue;
            }
            download(directory, downloadFile, saveDirectory, ftpClient);
        }
    }

    /**
     * 列出目录下的文件
     *
     * @param directory
     *            要列出的目录
     *
     * @return list 文件名列表
     *
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public List<String> listFiles(String directory, ChannelSftp ftpClient) throws Exception {
        Vector fileList;
        List<String> fileNameList = new ArrayList<>();

        fileList = ftpClient.ls(directory);

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
     * Description: 从FTP服务器下载同一目录夹下多个文件
     *
     * @param ftpBean           FtpBean
     * @return result            0: 执行出错
     *                            1: 没有找到指定文件
     *                            2: 执行成功
     */
    public int downFiles(FtpBean ftpBean , ChannelSftp ftpClient,List<String> fileNames) throws Exception {
        logger.info(ftpBean.getDown_remotepath() + "  Ftp 下载多个文件开始");

        logger.info("hostname=" + ftpBean.getUrl() + "  port=" + ftpBean.getPort());
        logger.info("  remotePath=" + ftpBean.getDown_remotepath());
        logger.info("  localPath=" + ftpBean.getDown_localpath());
        logger.info("  fileName=" + fileNames);
        int result = 0;
        for (String fileName : fileNames) {
            try {
                download(ftpBean.getDown_remotepath(), fileName, ftpBean.getDown_localpath(), ftpClient);
                result = 2;
            } catch (Exception e) {
                throw new RuntimeException(fileName + "文件下载失败:" + e);
            }
        }
        logger.info(ftpBean.getDown_remotepath() + "  Ftp 下载多个文件结束");
        return result;
    }


    /**
     * 将FTP服务器目录夹下所有文件删除
     * @param ftpBean           FtpBean
     * @param ftpClient         ChannelSftp
     */
    public void delFilesByFolder(FtpBean ftpBean , ChannelSftp ftpClient) throws Exception {
        logger.info(ftpBean.getDown_remotepath() + "/" + " 删除Ftp下载文件开始");

        // 获取文件列表
        List<String> fs = listFiles(ftpBean.getDown_remotepath(), ftpClient);
        for (String ff : fs) {
            delete(ftpBean.getDown_remotepath(), ff, ftpClient);
        }

        logger.info(ftpBean.getDown_remotepath() + "/" + " 删除Ftp下载文件结束");

    }

    /**
     * 删除文件
     *
     * @param directory
     *            要删除文件所在目录
     * @param deleteFile
     *            要删除的文件
     *
     * @throws Exception
     */
    public void delete(String directory, String deleteFile, ChannelSftp ftpClient) throws Exception {
        logger.info(directory + "/" + deleteFile + " 删除");
        ftpClient.cd(directory);
        ftpClient.rm(deleteFile);
    }


    /**
     * 将FTP服务器上文件删除
     * @param ftpBean           FtpBean
     * @param ftpClient         ChannelSftp
     * @param fileName         文件名
     */
    public void delOneFile(FtpBean ftpBean , ChannelSftp ftpClient, String fileName) throws Exception {
        String filePathName = ftpBean.getDown_remotepath() + "/" +  fileName;
        logger.info(filePathName  + " 删除Ftp下载文件开始");

        delete(ftpBean.getDown_remotepath(), fileName, ftpClient);

        logger.info(filePathName  + " 删除Ftp下载文件结束");
    }

    /**
     * 将FTP服务器上文件移动位置
     * @param ftpBean           FtpBean
     * @param ftpClient         ChannelSftp
     * @param fileName         文件名
     */
    public void removeOneFile(FtpBean ftpBean, ChannelSftp ftpClient, String fileName) throws SftpException {
        String bakPath = ftpBean.getRemote_bak_path();
        String bakFileName = fileName.substring(0, fileName.lastIndexOf(".")) + "_" + DateTimeUtil.getNow(DateTimeUtil.DATE_TIME_FORMAT_2) + fileName.substring(fileName.lastIndexOf("."));
        String srcFilePathName = ftpBean.getDown_remotepath() + "/" + fileName;
        String tgtFilePathName = bakPath + "/" + bakFileName;
        createDir(bakPath, ftpClient);
        try {
            logger.info("移动 " + srcFilePathName + " 到 " + tgtFilePathName + " 开始。");
            if(StringUtils.isEmpty(fileName)) {
                logger.error("fileName 不能为空或null");
                return;
            }
            ftpClient.rename(srcFilePathName, tgtFilePathName);
            logger.info("移动 " + srcFilePathName + " 到 " + tgtFilePathName + " 结束。");
        }catch (Exception e){
            throw new RuntimeException("移动文件" + srcFilePathName + " 到 " + tgtFilePathName + "失败！");
        }
    }

    /**
     * 读取目标目录夹下所有文件名, 过滤掉文件夹
     * @param ftpBean           FtpBean
     * @param ftpClient         ChannelSftp
     * @param folderPath        文件路径
     */
    public List<String> getFolderFileNames(FtpBean ftpBean, ChannelSftp ftpClient, String folderPath) throws Exception {
        logger.info(folderPath  + " 读取目标目录夹下所有文件名开始");
        List<String> fileNames = new ArrayList<>();
        // 获取文件列表
        List<String> fs = listFiles(folderPath, ftpClient);
        for (String ff : fs) {
            fileNames.add(ff);
        }
        logger.info(folderPath  + " 读取目标目录夹下所有文件名结束");
        return fileNames;
    }


    /**
     * Description: 向FTP服务器上传文件
     *
     * @param ftpBean           FtpBean
     * @return ChannelSftp        ChannelSftp
     *  成功返回true，否则返回false
     */
    public  boolean uploadFileForSFTP(FtpBean ftpBean,ChannelSftp ftpClient) throws Exception {
        logger.info(ftpBean.getDown_filename() + "  SFtp 上传文件开始");
        logger.info("hostname=" + ftpBean.getUrl() + "  port=" + ftpBean.getPort());
        logger.info("  uploadPath=" + ftpBean.getUpload_path());
        logger.info("  uploadLocalPath=" + ftpBean.getUpload_localpath());
        logger.info("  uploadFileName=" + ftpBean.getUpload_filename());
        boolean result = false;

        if(ftpBean.getUpload_filename() != null){
            String localFile = ftpBean.getUpload_localpath() + this.seperator + ftpBean.getUpload_filename();
            File file = new File(localFile);

            if(file.isFile()){
                //String remoteFile = ftpBean.getUpload_path() + this.seperator + ftpBean.getUpload_filename();
                //File rfile = new File(remoteFile);
                //String rpath = rfile.getParent();
                try {
                    //createDir(rpath, ftpClient);
                    try (InputStream in = new FileInputStream(file);){
                        ftpClient.put(in, file.getName());
                        result = true;
                        logger.info(ftpBean.getUpload_filename() + " SFtp 上传文件成功");
                    }
                    //ftpClient.put(new FileInputStream(file), file.getName());
                }
                catch (Exception e) {
                    logger.info(ftpBean.getUpload_filename() + " SFtp 上传文件失败");
                    throw e;
                }
            }
        }
        logger.info(ftpBean.getUpload_filename() + " SFtp 上传文件结束");

        return result;
    }

    /**
     * create Directory
     */
    public void createDirForSftp(String filepath, ChannelSftp sftp) throws SftpException {
        try {
            sftp.cd(filepath);
        } catch (SftpException e1) {
            sftp.mkdir(filepath);
        }
    }
}
