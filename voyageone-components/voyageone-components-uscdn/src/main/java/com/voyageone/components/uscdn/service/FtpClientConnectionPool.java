package com.voyageone.components.uscdn.service;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.IOException;
import java.util.Vector;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class FtpClientConnectionPool {

    private Lock lock = new ReentrantLock();// 锁
    private Vector<FTPClient> locked, unlocked;

    private String url;
    private String userName;
    private String password;
    private String encoding = "utf-8";

    int connectTimeout = 30 * 1000;
    int dataTimeout = 30 * 10000;

    public FtpClientConnectionPool(String url, String userName, String password) {
        locked = new Vector<>();
        unlocked = new Vector<>();
        this.url = url;
        this.userName = userName;
        this.password = password;
    }

    public FtpClientConnectionPool(String url, String userName, String password, String encoding) {
        locked = new Vector<>();
        unlocked = new Vector<>();
        this.url = url;
        this.userName = userName;
        this.password = password;
        this.encoding = encoding;
    }

    public FtpClientConnectionPool(String url, String userName, String password, String encoding, int connectTimeout, int dataTimeout) {
        locked = new Vector<>();
        unlocked = new Vector<>();
        this.url = url;
        this.userName = userName;
        this.password = password;
        this.connectTimeout = connectTimeout;
        this.dataTimeout = dataTimeout;
        this.encoding = encoding;
    }

    public FTPClient checkOut() throws IOException {
        lock.lock();// 取得锁
        for (int i = unlocked.size() - 1; i >= 0; i--) {
            FTPClient f = unlocked.get(i);
            unlocked.remove(f);
            if (!timedOut(f)) {
                locked.add(f);
                lock.unlock();// 释放锁
                return f;
            } else {
                try {
                    f.disconnect();
                } catch (Exception ignored) {
                }
            }
        }
        // No active instance - create new
        FTPClient newF = create();
        locked.add(newF);
        lock.unlock();// 释放锁
        return newF;
    }

    public void checkIn(FTPClient f) {
        lock.lock();// 取得锁lock.lock();// 取得锁
        locked.remove(f);
        unlocked.add(f);
        lock.unlock();// 释放锁
    }

    private FTPClient create() throws IOException {
        FTPClient newInstance = new FTPClient();
        newInstance.setControlEncoding(encoding);
        newInstance.setConnectTimeout(connectTimeout);
        newInstance.setDataTimeout(dataTimeout);
        newInstance.connect(url);
        newInstance.login(userName, password);
        // 设置文件传输类型为二进制
        newInstance.setFileType(FTPClient.BINARY_FILE_TYPE);
        // 获取ftp登录应答代码
        int reply = newInstance.getReplyCode();
        // 验证是否登陆成功
        if (!FTPReply.isPositiveCompletion(reply)) {
            throw new RuntimeException("FTPReply.isPositiveCompletion error");
        }
        return newInstance;
    }

    private Boolean timedOut(FTPClient f) {
        try {
            f.changeWorkingDirectory("/");
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return true;
        }
    }
}