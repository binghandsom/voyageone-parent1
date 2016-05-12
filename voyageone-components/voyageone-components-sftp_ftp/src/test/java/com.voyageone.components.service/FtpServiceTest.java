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
public class FtpServiceTest {

    @Autowired
    private FtpService ftpService;

    @Test
    public void testStoreFile() throws Exception {
        String url = "69.160.63.67";
        String port="21";
        // ftp连接usernmae
        String username = "images@xpairs.com";
        // ftp连接password
        String password = "voyageone5102";
        String encoding="iso-8859-1";
        String fileName="test13.jpg";
        String destFolder="/test/";
        int timeOut= 60000;
        InputStream inputStream= new FileInputStream( new File("f:/TestFTP.txt"));
        ftpService.storeFile(url,port, username, password, fileName, destFolder, inputStream, encoding, timeOut);
    }

    @Test
    public void testDownloadFile() throws Exception {
        String url = "69.160.63.67";
        String port="21";
        // ftp连接usernmae
        String username = "images@xpairs.com";
        // ftp连接password
        String password = "voyageone5102";
        String encoding="iso-8859-1";
        String fileName = "/test/test13.jpg";
        String localpath="f:";
        String remotepath = "/test/test13.jpg";
        ftpService.downloadFile(url,port, username, password, fileName, localpath, remotepath, encoding);
    }
}