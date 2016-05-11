package com.voyageone.common.util;

import com.voyageone.common.configs.beans.FtpBean;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author fred
 */
public class FtpUtil {

    private final static Logger logger = LoggerFactory.getLogger(FtpUtil.class);

    //private  FTPClient ftpClient = new FTPClient();
    private  String encoding = System.getProperty("file.encoding");

    private final String basCoding = "iso-8859-1";
    /**
     * Description: 向FTP服务器上传文件
     *
     * @param ftpBean           FtpBean
     * @return 成功返回true，否则返回false
     */
    public boolean uploadFile(FtpBean ftpBean,FTPClient ftpClient) throws IOException {
        logger.info(ftpBean.getUpload_filename() + "  Ftp 上传文件开始");
        logger.info("hostname=" + ftpBean.getUrl() + "  port=" + ftpBean.getPort());
        logger.info("  uploadPath=" + ftpBean.getUpload_path());
        logger.info("  uploadLocalPath=" + ftpBean.getUpload_localpath());
        logger.info("  uploadFileName=" + ftpBean.getUpload_filename());
        boolean result = false;

        // 转移工作目录至指定目录下
        boolean change = ftpClient.changeWorkingDirectory(ftpBean.getUpload_path());
        // 设置PassiveMode传输
        ftpClient.enterLocalPassiveMode();
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
        if (change) {
            File f = new File(ftpBean.getUpload_localpath() + "/" + ftpBean.getUpload_filename());
            try (InputStream in = new FileInputStream(f);) {
                result = ftpClient.storeFile(ftpBean.getUpload_filename(), in);
            }

            if (result) {
                logger.info("上传成功!");
            }
        }

        logger.info(ftpBean.getUpload_filename()  + " Ftp 上传文件结束");
        return result;
    }

    /**
     * Description: 连接FTP服务器
     *
     * @param ftpBean           FtpBean
     * @return FTPClient        FTPClient
     */
    public FTPClient linkFtp(FtpBean ftpBean) throws IOException {
        FTPClient ftpClient = new FTPClient();
        logger.info(ftpBean.getUrl() + "  Ftp 连接开始");
        logger.info("hostname=" + ftpBean.getUrl() + "  port=" + ftpBean.getPort() );
        //boolean result = false;
        try {
            int reply;
            ftpClient.setControlEncoding(encoding);
            ftpClient.setConnectTimeout(30000);
            ftpClient.setDataTimeout(300000);
            //连接FTP服务器
            if (StringUtils.isNullOrBlank2(ftpBean.getPort())){
                // 如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器
                ftpClient.connect(ftpBean.getUrl());
            }else{
                ftpClient.connect(ftpBean.getUrl(), Integer.parseInt(ftpBean.getPort()));
            }

            ftpClient.login(ftpBean.getUsername(),ftpBean.getPassword());// 登录

            // 设置文件传输类型为二进制
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            // 获取ftp登录应答代码
            reply = ftpClient.getReplyCode();
            // 验证是否登陆成功
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftpClient.disconnect();
                logger.info("FTP server refused connection.");
                return null;
            } else{
                //result = true;
                logger.info("用户登录成功");
            }
        } catch (IOException e) {
            disconnectFtp(ftpClient);
            throw e;
        }
        logger.info(ftpBean.getUrl()  + " Ftp 连接结束");
        return ftpClient;
    }

    /**
     * Description: 断开FTP服务器
     *
     * @param ftpClient         FTPClient
     */
    public void disconnectFtp(FTPClient ftpClient) throws IOException {
        logger.info("Ftp 断开开始");
        if (ftpClient.isConnected()) {
            //ftpClient.logout();

            ftpClient.disconnect();
            logger.info("Ftp 断开成功");
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
    public int downFile(FtpBean ftpBean , FTPClient ftpClient) throws IOException {
        logger.info(ftpBean.getDown_filename() + "  Ftp 下载文件开始");

        logger.info("hostname=" + ftpBean.getUrl() + "  port=" + ftpBean.getPort());
        logger.info("  remotePath=" + ftpBean.getDown_remotepath());
        logger.info("  localPath=" + ftpBean.getDown_localpath());
        logger.info("  fileName=" + ftpBean.getDown_filename());
        logger.info("  getFile_coding=" + ftpBean.getFile_coding());

        int result;
        //定位FTP目录夹
        changeFolder(ftpBean,ftpClient);
        // 获取文件列表
        FTPFile[] fs = ftpClient.listFiles(ftpBean.getDown_remotepath());
        logger.info("  fs.length=" +fs.length);

        int fileCount = 0;
        for (FTPFile ff : fs) {
            //下载文件为空，下载全部文件
            if (StringUtils.isNullOrBlank2(ftpBean.getDown_filename())){
                downloadFile(ftpBean.getDown_localpath(), ff, ftpClient);
                fileCount= fileCount + 1;
            }else{
                //下载指定文件
                if (ff.getName().equals(ftpBean.getDown_filename())) {
                    downloadFile(ftpBean.getDown_localpath(), ff, ftpClient);
                    fileCount= fileCount + 1;
                    break;
                }
            }
        }
        if (fileCount > 0){
            result = 2;
        }else{
            result = 1;
            logger.info(ftpBean.getDown_remotepath() + "/" + ftpBean.getDown_filename()  + " Ftp 下载文件不存在");
        }

        logger.info(ftpBean.getDown_filename()  + " Ftp 下载文件结束");
        return result;
    }

    /**
     * Description: 从FTP服务器下载同一目录夹下多个文件
     *
     * @param ftpBean           FtpBean
     * @return result            0: 执行出错
     *                            1: 没有找到指定文件
     *                            2: 执行成功
     */
    public int downFiles(FtpBean ftpBean , FTPClient ftpClient,List<String> fileNames) throws IOException {
        logger.info(ftpBean.getDown_remotepath() + "  Ftp 下载多个文件开始");

        logger.info("hostname=" + ftpBean.getUrl() + "  port=" + ftpBean.getPort());
        logger.info("  remotePath=" + ftpBean.getDown_remotepath());
        logger.info("  localPath=" + ftpBean.getDown_localpath());
        logger.info("  fileName=" + fileNames);

        int result;

        //定位FTP目录夹
        changeFolder(ftpBean,ftpClient);
        // 获取文件列表
        FTPFile[] fs = ftpClient.listFiles();
        int fileCount = 0;
        boolean downFileFlg;
        for (String fileName : fileNames) {
            downFileFlg = false;
            for (FTPFile ff : fs) {
                //下载指定文件
                if (ff.getName().equals(fileName)) {
                    downloadFile(ftpBean.getDown_localpath(), ff, ftpClient);
                    fileCount = fileCount + 1;
                    logger.info(ftpBean.getDown_remotepath() + "/" + fileName  + " Ftp 下载文件成功");
                    downFileFlg = true;
                    break;
                }
            }
            if (!downFileFlg){
                logger.info(ftpBean.getDown_remotepath() + "/" + fileName  + " Ftp 下载文件不存在");
            }
        }
        if (fileCount > 0){
            result = 2;
        }else{
            result = 1;
        }

        logger.info(ftpBean.getDown_remotepath() + "  Ftp 下载多个文件结束");
        return result;
    }

    /**
     * 定位FTP目录夹
     *
     */
    private void changeFolder(FtpBean ftpBean , FTPClient ftpClient) throws IOException {
        logger.info(ftpBean.getDown_remotepath() + "  Ftp 定位FTP目录夹开始");
        // 转移到FTP服务器目录至指定的目录下
        // 文件编码格式没有指定的情况，转换为iso-8859-1格式
        if (StringUtils.isNullOrBlank2(ftpBean.getFile_coding())) {
            ftpClient.changeWorkingDirectory(new String(ftpBean.getDown_remotepath().getBytes(encoding), basCoding));
        } else {
            // 文件编码格式有指定的情况，转换为指定格式
            ftpClient.changeWorkingDirectory(new String(ftpBean.getDown_remotepath().getBytes(encoding), ftpBean.getFile_coding()));
        }
        logger.info(ftpBean.getDown_remotepath() + "  Ftp 定位FTP目录夹结束");
    }


    /**
     * 将FTP服务器上文件下载到本地
     *
     */
    private void downloadFile(String filePath , FTPFile ftpFile,FTPClient ftpClient) throws IOException {
        OutputStream is = null;
        try {
            File localFile = new File(filePath + "/" + ftpFile.getName());
            is = new FileOutputStream(localFile);
            ftpClient.retrieveFile(ftpFile.getName(), is);
        }catch(Exception e){
            throw new RuntimeException(ftpFile.getName() + "文件下载失败！");
        }finally {
            if (is != null) {
                is.close();
            }
        }
    }

    /**
     * 将FTP服务器目录夹下所有文件删除
     * @param ftpBean           FtpBean
     * @param ftpClient         FTPClient
     */
    public void delFilesByFolder(FtpBean ftpBean , FTPClient ftpClient) throws IOException {
        boolean delSuccess;
        String filePathName = ftpBean.getDown_remotepath() + "/" +  ftpBean.getDown_filename();
        logger.info(filePathName  + " 删除Ftp下载文件开始");

        //定位FTP目录夹
        changeFolder(ftpBean,ftpClient);

        // 获取文件列表
        FTPFile[] fs = ftpClient.listFiles();
        for (FTPFile ff : fs) {
            filePathName = ftpBean.getDown_remotepath() +  "/"  + ff.getName();
            delSuccess = ftpClient.deleteFile(filePathName);
            if (!delSuccess) {
                logger.info(filePathName  + " 删除Ftp下载文件失败");

            }else{
                logger.info(filePathName  + " 删除Ftp下载文件成功");
            }
        }

        logger.info(ftpBean.getDown_remotepath() + "/" + " 删除Ftp下载文件结束");

    }

    /**
     * 将FTP服务器上文件删除
     * @param ftpBean           FtpBean
     * @param ftpClient         FTPClient
     * @param fileName         文件名
     */
    public void delOneFile(FtpBean ftpBean , FTPClient ftpClient, String fileName) throws IOException {
        boolean delSuccess = true;

        String filePathName = ftpBean.getDown_remotepath() + "/" +  fileName;
        logger.info(filePathName  + " 删除Ftp下载文件开始");

        delSuccess = ftpClient.deleteFile(filePathName);

        if (!delSuccess) {
            logger.info(filePathName  + " 删除Ftp下载文件失败");

        }else{
            logger.info(filePathName  + " 删除Ftp下载文件成功");
        }
        logger.info(filePathName  + " 删除Ftp下载文件结束");
    }

    /**
     * 将FTP服务器上文件移动位置
     * @param ftpBean           FtpBean
     * @param ftpClient         FTPClient
     * @param fileName         文件名
     */
    public void removeOneFile(FtpBean ftpBean, FTPClient ftpClient, String fileName) throws IOException {
        boolean rmvSuccess;
        String bakPath = ftpBean.getRemote_bak_path();
        if(ftpClient.makeDirectory(bakPath)){
            logger.info("创建目录 " + bakPath + " 成功！");
        }else {
            logger.info(bakPath + "目录已经存在无需创建！");
        }
        String bakFileName = fileName.substring(0, fileName.lastIndexOf(".")) + "_" + DateTimeUtil.getNow(DateTimeUtil.DATE_TIME_FORMAT_2) + fileName.substring(fileName.lastIndexOf("."));
        String srcFilePathName = ftpBean.getDown_remotepath() + "/" +  fileName;
        String tgtFilePathName = bakPath + "/" + bakFileName;
        if(StringUtils.isEmpty(fileName)) return;
        logger.info("移动 " + srcFilePathName  + " 到 " + tgtFilePathName + " 开始。");
        rmvSuccess = ftpClient.rename(srcFilePathName, tgtFilePathName);
        if (!rmvSuccess) {
            logger.info("移动文件失败！");
            throw new RuntimeException(fileName + "文件移动失败！");
        }else{
            logger.info("移动文件成功！");
        }
        logger.info("移动 " + srcFilePathName  + " 到 " + tgtFilePathName + " 结束。");
    }

    /**
     * 读取目标目录夹下所有文件名, 过滤掉文件夹
     * @param ftpBean           FtpBean
     * @param ftpClient         FTPClient
     * @param folderPath        文件路径
     */
    public List<String> getFolderFileNames(FtpBean ftpBean, FTPClient ftpClient, String folderPath)throws IOException {
        logger.info(folderPath  + " 读取目标目录夹下所有文件名开始");
        List<String> fileNames = new ArrayList<>();
        //定位FTP目录夹
        changeFolder(ftpBean, ftpClient);
        // 获取文件列表
        FTPFile[] fs = ftpClient.listFiles();
        for (FTPFile ff : fs) {
            if(ff.isFile()) {
                logger.info("fileName=" + ff.getName());
                fileNames.add(ff.getName());
            }else{
                logger.info("fileName=" + ff.getName() + "不是文件！");
            }
        }
        logger.info(folderPath  + " 读取目标目录夹下所有文件名结束");
        return fileNames;
    }
}
