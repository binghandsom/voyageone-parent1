package com.voyageone.common.configs.beans;

import java.io.InputStream;

/**
 * Created by fred on 2015/6/24.
 */
public class FtpBean {

    /**
     * FTP服务器hostname
     */
    private String url;
    /**
     * FTP服务器端口
     */
    private String port;
    /**
     * FTP登录账号
     */
    private String username;
    /**
     * FTP登录密码
     */
    private String password;
    /**
     * FTP服务器保存目录,如果是根目录则为“/”
     */
    private String upload_path;

    /**
     * 上传到FTP服务器上的文件路径
     */
    private String upload_localpath;
    /**
     * 上传到FTP服务器上的文件路径
     */
    private String upload_localfilename;

    /**
     * 上传到FTP服务器上的备份文件路径
     */
    private String upload_local_bak_path;
    /**
     * 上传到FTP服务器上的文件名
     */
    private String upload_filename;
    /**
     * 本地文件输入流
     */
    private InputStream upload_input;

    /**
     * FTP服务器上的相对路径
     */
    private String  down_remotepath;
    /**
     * 要下载的文件名
     */
    private String down_filename;
    /**
     * 下载后保存到本地的路径
     */
    private String down_localpath;
    /**
     * 下载后保存到本地的备份路径
     */
    private String down_local_bak_path;
    /**
     * 文件名编码格式
     */
    private String file_coding;
    /**
     *ftp文件下载后远程服务器的备份路径
     */
    private String remote_bak_path;

    public String getRemote_bak_path() {
        return remote_bak_path;
    }

    public void setRemote_bak_path(String remote_bak_path) {
        this.remote_bak_path = remote_bak_path;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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

    public String getUpload_path() {
        return upload_path;
    }

    public void setUpload_path(String upload_path) {
        this.upload_path = upload_path;
    }

    public String getUpload_filename() {
        return upload_filename;
    }

    public void setUpload_filename(String upload_filename) {
        this.upload_filename = upload_filename;
    }

    public InputStream getUpload_input() {
        return upload_input;
    }

    public void setUpload_input(InputStream upload_input) {
        this.upload_input = upload_input;
    }

    public String getDown_remotepath() {
        return down_remotepath;
    }

    public void setDown_remotepath(String down_remotepath) {
        this.down_remotepath = down_remotepath;
    }

    public String getDown_filename() {
        return down_filename;
    }

    public void setDown_filename(String down_filename) {
        this.down_filename = down_filename;
    }

    public String getDown_localpath() {
        return down_localpath;
    }

    public void setDown_localpath(String down_localpath) {
        this.down_localpath = down_localpath;
    }

    public String getDown_local_bak_path() {
        return down_local_bak_path;
    }

    public void setDown_local_bak_path(String down_local_bak_path) {
        this.down_local_bak_path = down_local_bak_path;
    }

    public String getFile_coding() {
        return file_coding;
    }

    public void setFile_coding(String file_coding) {
        this.file_coding = file_coding;
    }

    public String getUpload_localpath() {
        return upload_localpath;
    }

    public void setUpload_localpath(String upload_localpath) {
        this.upload_localpath = upload_localpath;
    }

    public String getUpload_localfilename() {
        return upload_localfilename;
    }

    public void setUpload_localfilename(String upload_localfilename) {
        this.upload_localfilename = upload_localfilename;
    }

    public String getUpload_local_bak_path() {
        return upload_local_bak_path;
    }

    public void setUpload_local_bak_path(String upload_local_bak_path) {
        this.upload_local_bak_path = upload_local_bak_path;
    }
}
