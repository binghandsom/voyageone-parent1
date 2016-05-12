package com.voyageone.components.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * @author aooer 2016/5/12.
 * @version 2.0.0
 * @since 2.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context.xml")
public class SFtpServiceTest {

    @Autowired
    private SFtpService sFtpService;

    @Test
    public void testStoreFile() throws Exception {
        String url = "image.voyageone.com.cn";
        String port="22";
        // ftp连接usernmae
        String username = "voyageone-cms-sftp";
        // ftp连接password
        String password = "Li48I-22aBz";
        String encoding = "iso-8859-1";
        String fileName = "test13.jpg";
        String destFolder = "/test/";
        InputStream inputStream = new FileInputStream(new File("f:/TestFTP.txt"));
        sFtpService.storeFile(url,port, username, password, fileName, destFolder, inputStream, encoding);
    }

    @Test
    public void testDownloadFile() throws Exception {
        String url = "image.voyageone.com.cn";
        String port="22";
        // ftp连接usernmae
        String username = "voyageone-cms-sftp";
        // ftp连接password
        String password = "Li48I-22aBz";
        String encoding = "iso-8859-1";
        String fileName = "/test/test13.jpg";
        String localpath="f:";
        String remotepath = "/test/";
        sFtpService.downloadFile(url,port, username, password, fileName, localpath, remotepath, encoding);
    }
}