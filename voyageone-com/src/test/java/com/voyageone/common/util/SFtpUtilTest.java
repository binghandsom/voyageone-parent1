package com.voyageone.common.util;

import com.jcraft.jsch.ChannelSftp;
import com.voyageone.common.configs.beans.FtpBean;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;

/**
 * Created by DELL on 2016/5/6.
 */
public class SFtpUtilTest {

    /**
     * 配置信息
     */
    private FtpBean formatFtpBean(){

        String url = "image.voyageone.com.cn";
        // ftp连接port
        String port = "22";
        // ftp连接usernmae
        String username = "voyageone-cms-sftp";
        // ftp连接password
        String password = "Li48I-22aBz";

//        // ftp连接地址
//        url = ThirdPartyConfigs.getVal1(orderChannelID, ftp_address);
//        // ftp连接port
//        port = ThirdPartyConfigs.getVal1(orderChannelID, ftp_port);
//        // ftp连接usernmae
//        username = ThirdPartyConfigs.getVal1(orderChannelID, ftp_usernmae);
//        // ftp连接password
//        password = ThirdPartyConfigs.getVal1(orderChannelID, ftp_password);

        FtpBean ftpBean = new FtpBean();
        ftpBean.setPort(port);
        ftpBean.setUrl(url);
        ftpBean.setUsername(username);
        ftpBean.setPassword(password);
        ftpBean.setFile_coding("iso-8859-1");
        return ftpBean;
    }

    @Test
    public void testUploadFile() throws Exception {
        FtpBean ftpBean = formatFtpBean();
        ftpBean.setUpload_filename("test11.jpg");
        //ftpBean.setUpload_path("/");
        File uploadFile = new File("d:/snusa-detail_20.png");
        ftpBean.setUpload_input(new FileInputStream(uploadFile));

        SFtpUtil ftpUtil = new SFtpUtil();
        //建立连接
        ChannelSftp ftpClient = ftpUtil.linkFtp(ftpBean);
        boolean isSuccess = ftpUtil.uploadFile(ftpBean, ftpClient);
        if (!isSuccess) {
            throw new Exception("upload error");
        }
    }
}
