package com.voyageone.components.ftp.bean;

import com.voyageone.common.util.StringUtils;

import java.io.InputStream;

/**
 * BaseFtpBean
 *
 * @author chuanyu.liang 2016/5/10.
 * @version 2.0.0
 * @since 2.0.0
 */
public class BaseFtpBean {
    /**
     * FTP服务器hostname
     */
    protected String hostname;
    /**
     * FTP服务器端口
     */
    protected int port;
    /**
     * FTP登录账号
     */
    protected String username;
    /**
     * FTP登录密码
     */
    protected String password;
    /**
     * timeout
     */
    protected int timeout = 120000;

    /**
     * 文件名编码格式
     */
    private String coding = "iso-8859-1";

    public BaseFtpBean() {
    }

    public BaseFtpBean(BaseFtpBean baseFtpBean) {
        this.hostname = baseFtpBean.getHostname();
        this.port = baseFtpBean.getPort();
        this.username = baseFtpBean.getUsername();
        this.password = baseFtpBean.getPassword();
        this.timeout = baseFtpBean.getTimeout();
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }


    public String getCoding() {
        return coding;
    }

    public void setCoding(String coding) {
        this.coding = coding;
    }

    public void checkParam() {
        if (StringUtils.isEmpty(hostname)) {
            throw new RuntimeException("hostname not found.");
        }
        if (StringUtils.isEmpty(username)) {
            throw new RuntimeException("username not found.");
        }
        if (StringUtils.isEmpty(password)) {
            throw new RuntimeException("password not found.");
        }
    }
}
