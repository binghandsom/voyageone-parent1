package com.voyageone.components;

import com.voyageone.common.configs.beans.FtpBean;

import java.io.InputStream;

/**
 * @author aooer 2016/5/12.
 * @version 2.0.0
 * @since 2.0.0
 */
public abstract class FileTransferProtocol extends ComponentBase {

    /**
     * 构造ftpBean
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
    protected FtpBean buildStoreBean(String url, String port, String userName, String password, String fileName, String destFolder, InputStream uploadInputStream, String encoding) {
        return new FtpBean() {{
            setPort(port);// ftp连接port
            setUrl(url);// ftp连接url
            setUsername(userName);// ftp连接usernmae
            setPassword(password);// ftp连接password
            setFile_coding(encoding);// ftp连接上传文件编码
            setUpload_filename(fileName); //文件名称
            setUpload_path(destFolder);//FTP服务器保存目录设定
            setUpload_input(uploadInputStream); //上传文件流
        }};
    }

    /**
     * 构造ftpBean
     *
     * @param url        host
     * @param port       port
     * @param userName   user
     * @param password   password
     * @param fileName   uplaod fileName
     * @param localPath  local path
     * @param remotePath remote path
     * @param encoding   file encoding
     * @return ftpBean
     */
    protected FtpBean buildDownloadBean(String url, String port, String userName, String password, String fileName, String localPath, String remotePath, String encoding) {
        return new FtpBean() {{
            setPort(port);// ftp连接port
            setUrl(url);// ftp连接url
            setUsername(userName);// ftp连接usernmae
            setPassword(password);// ftp连接password
            setFile_coding(encoding);// ftp连接上传文件编码
            setDown_filename(fileName); //下载文件名
            setDown_localpath(localPath); //本地地址
            setDown_remotepath(remotePath); //远程地址
        }};
    }

    /**
     * 上传文件
     * <p>
     * 子类构造ftpBean
     * 与上边的方法对比
     *
     * @param url               host
     * @param port              port
     * @param userName          user
     * @param password          password
     * @param fileName          uplaod fileName
     * @param destFolder        upload folder
     * @param uploadInputStream InputStream
     * @param encoding          file encoding
     * @param timeOut           timeOut
     * @return upload result
     */
    protected abstract boolean storeFile(String url, String port, String userName, String password, String fileName, String destFolder, InputStream uploadInputStream, String encoding, int timeOut);

    /**
     * 下载文件
     * <p>
     * 子类构造ftpBean
     * 与上边的方法对比 缺失端口号port
     *
     * @param url        host
     * @param port       port
     * @param userName   user
     * @param password   password
     * @param fileName   uplaod fileName
     * @param localPath  localPath
     * @param remotePath remotePath
     * @param encoding   file encoding
     * @return upload result
     */
    protected abstract int downloadFile(String url, String port, String userName, String password, String fileName, String localPath, String remotePath, String encoding);
}
