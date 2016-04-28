package com.voyageone.components.uscdn.service;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.util.StringUtils;

import java.io.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class USCDNClient {
    private String url;
    private String userName;
    private String password;

    private Lock lock = new ReentrantLock();// 锁
    private static FtpClientConnectionPool ftpClientConnectionPool;

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

    public void uploadFile(String usCdnFilePath, String localFilePath) throws IOException {
        int lastIndex = usCdnFilePath.lastIndexOf("/");
        String WorkingDirectory = usCdnFilePath.substring(0, lastIndex);//文件目录
        String fileName = usCdnFilePath.substring(lastIndex + 1);//文件名
        FTPClient ftpClient = null;
        InputStream in = null;
        try {
            ftpClient = createftp(this.getUrl(), this.getUserName(), this.getPassword());
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
            if (in != null) {
                in.close();
            }
            if (ftpClient != null) {
                ftpClientConnectionPool.checkIn(ftpClient);
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

    private FTPClient createftp(String url, String userName, String password) throws IOException {
        lock.lock();// 取得锁
        if (ftpClientConnectionPool == null) {
            ftpClientConnectionPool = new FtpClientConnectionPool(url, userName, password, "utf-8");
        }
        lock.unlock();// 释放锁
        return ftpClientConnectionPool.checkOut();
    }
}
