package com.voyageone.components.uscdn.service;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.util.StringUtils;

import java.io.*;

/**
 * Created by dell on 2016/4/21.
 */
public class USCDNClient {
    private String url;// "ftp.vny.FB70.omicroncdn.net";
    private String userName;//"admin@voyageone.com";
    private String password;//"Voyage1#";

    int connectTimeout = 30 * 1000;
    int dataTimeout = 30 * 10000;

    public USCDNClient(String url, String userName, String password, String workingDirectory) {
        this.setUrl(url);
        this.setUserName(userName);
        this.setPassword(password);
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getConnectTimeout() {
        return this.connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public int getDataTimeout() {
        return dataTimeout;
    }

    public void setDataTimeout(int dataTimeout) {
        this.dataTimeout = dataTimeout;
    }

    public void uploadFile(String usCdnFilePath, String localFilePath) throws IOException {
        int lastIndex = usCdnFilePath.lastIndexOf("/");
        String WorkingDirectory = usCdnFilePath.substring(0, lastIndex);//文件目录
        String fileName = usCdnFilePath.substring(lastIndex + 1);//文件名
        FTPClient ftpClient = null;
        InputStream in = null;
        try {
            ftpClient = createftp(this.getUrl(), this.getUserName(), this.getPassword(), this.connectTimeout, this.dataTimeout);
            boolean change = ftpClient.changeWorkingDirectory(WorkingDirectory);
            if (!change) {
                mkdirPath(WorkingDirectory, ftpClient);
                if (ftpClient.changeWorkingDirectory(WorkingDirectory)) {
                    throw new RuntimeException("ftpClient.changeWorkingDirectory error");
                }
            }
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            File f = new File(localFilePath);
            in = new FileInputStream(f);
            ftpClient.storeFile(fileName, in);
        } finally {
            if (ftpClient != null) {
                ftpClient.disconnect();
            }
            if (in != null) {
                in.close();
            }
        }
    }

    public static void mkdirPath(String path, FTPClient ftpClient) throws IOException {
        String[] paths = path.split("/");
        StringBuilder fullPath = new StringBuilder();
        boolean change;
        for (String path1 : paths) {
            if (StringUtils.isEmpty(path1)) {
                continue;
            }
            fullPath.append("/").append(path1);
            change = ftpClient.changeWorkingDirectory(fullPath.toString());
            if (!change) {
                ftpClient.makeDirectory(fullPath.toString());
                System.out.println("创建目录为：" + fullPath.toString());
            }
        }
    }

    private FTPClient createftp(String url, String userName, String Password, int connectTimeout, int dataTimeout) throws IOException {
        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.setControlEncoding("utf-8");
            ftpClient.setConnectTimeout(connectTimeout);
            ftpClient.setDataTimeout(dataTimeout);
            ftpClient.connect(url);
            ftpClient.login(userName, Password);// 登录

            // 设置文件传输类型为二进制
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            // 获取ftp登录应答代码
            int reply = ftpClient.getReplyCode();
            // 验证是否登陆成功
            if (!FTPReply.isPositiveCompletion(reply)) {
                throw new RuntimeException("FTPReply.isPositiveCompletion error");
            }
            return ftpClient;
        } catch (IOException e) {
            //disconnect(ftpClient);
            ftpClient.disconnect();
            throw e;
        }
    }
}
