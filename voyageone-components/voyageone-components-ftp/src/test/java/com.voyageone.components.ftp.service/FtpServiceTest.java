package com.voyageone.components.ftp.service;

import com.voyageone.components.ftp.FtpComponentFactory;
import com.voyageone.components.ftp.FtpConstants;
import com.voyageone.components.ftp.bean.FtpFileBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author chuanyu.liang 2016/5/12.
 * @version 2.0.0
 * @since 2.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context.xml")
public class FtpServiceTest {

    @Test
    public void testStoreFiles() throws Exception {
        BaseFtpComponent ftpComponent = FtpComponentFactory.getFtpComponent(FtpConstants.FtpConnectEnum.NEXCESS_FTP);

        ftpComponent.openConnect();

        ftpComponent.uploadFile(new FtpFileBean("d:/", "testaacc-111.jpg", "/test/test1/test2"));
        ftpComponent.uploadFile(new FtpFileBean("d:/", "snusa-detail_20.png", "/test/test1/test2/"));

        ftpComponent.closeConnect();

    }
    @Test
    public void testUploadFile1() throws Exception {
        BaseFtpComponent ftpComponent = FtpComponentFactory.getFtpComponent(FtpConstants.FtpConnectEnum.NEXCESS_FTP);

        ftpComponent.openConnect();

        ftpComponent.uploadFile(new FtpFileBean("d:/", "testaacc-111.jpg", "/test/", "testaacc-111.jpg"));
        ftpComponent.uploadFile(new FtpFileBean("d:/", "snusa-detail_20.png", "/test/", "snusa-detail_20.png"));

        ftpComponent.closeConnect();
    }

    @Test
    public void testUploadFile2() throws Exception {
        BaseFtpComponent ftpComponent = FtpComponentFactory.getFtpComponent(FtpConstants.FtpConnectEnum.NEXCESS_FTP);

        ftpComponent.openConnect();

        ftpComponent.uploadFile(new FtpFileBean(new FileInputStream(new File("d:/testaacc-111.jpg")), "/test", "testaacc-111.jpg"));
        ftpComponent.uploadFile(new FtpFileBean(new FileInputStream(new File("d:/snusa-detail_20.png")), "/test", "snusa-detail_20.png"));

        ftpComponent.closeConnect();
    }

    @Test
    public void testUploadFile3() throws Exception {
        BaseFtpComponent ftpComponent = FtpComponentFactory.getFtpComponent(FtpConstants.FtpConnectEnum.NEXCESS_FTP);

        ftpComponent.uploadFile(new FtpFileBean("d:/", "testaacc-111.jpg", "/test/", "testaacc-111.jpg"));
        ftpComponent.uploadFile(new FtpFileBean("d:/", "snusa-detail_20.png", "/test/", "snusa-detail_20.png"));

        ftpComponent.closeConnect();
    }

    @Test
    public void testUploadFile4() throws Exception {
        BaseFtpComponent ftpComponent = FtpComponentFactory.getFtpComponent(FtpConstants.FtpConnectEnum.NEXCESS_FTP);

        ftpComponent.openConnect();

        List<FtpFileBean> fileBeans = new ArrayList<>();
        fileBeans.add(new FtpFileBean("d:/", "testaacc-111.jpg", "/test/", "testaacc-111.jpg"));
        fileBeans.add(new FtpFileBean("d:/", "snusa-detail_20.png", "/test/", "snusa-detail_20.png"));

        ftpComponent.uploadFiles(fileBeans);

        ftpComponent.closeConnect();
    }

    @Test
    public void testDownloadFile1() throws Exception {
        BaseFtpComponent ftpComponent = FtpComponentFactory.getFtpComponent(FtpConstants.FtpConnectEnum.NEXCESS_FTP);

        ftpComponent.openConnect();

        ftpComponent.downloadFile(new FtpFileBean("d:/", "testaacc-112.jpg", "/test", "testaacc-111.jpg"));
        ftpComponent.downloadFile(new FtpFileBean("d:/", "snusa-detail_21.png", "/test", "snusa-detail_20.png"));

        ftpComponent.closeConnect();
    }

    @Test
    public void testDownloadFile2() throws Exception {
        BaseFtpComponent ftpComponent = FtpComponentFactory.getFtpComponent(FtpConstants.FtpConnectEnum.NEXCESS_FTP);

        ftpComponent.openConnect();

        List<FtpFileBean> fileBeans = new ArrayList<>();
        fileBeans.add(new FtpFileBean("d:/", "testaacc-111.jpg", "/test/", "testaacc-111.jpg"));
        fileBeans.add(new FtpFileBean("d:/", "snusa-detail_20.png", "/test/", "snusa-detail_20.png"));

        ftpComponent.downloadFiles(fileBeans);

        ftpComponent.closeConnect();
    }

    @Test
    public void testListFiles() throws Exception {
        BaseFtpComponent ftpComponent = FtpComponentFactory.getFtpComponent(FtpConstants.FtpConnectEnum.NEXCESS_FTP);

        ftpComponent.openConnect();

        List<String> lst = ftpComponent.listFilePath("/test");

        ftpComponent.closeConnect();

        for (String fileName : lst) {
            System.out.println(fileName);
        }
    }

    @Test
    public void testDownloadFileDir() throws Exception {
        BaseFtpComponent ftpComponent = FtpComponentFactory.getFtpComponent(FtpConstants.FtpConnectEnum.NEXCESS_FTP);

        ftpComponent.openConnect();

        List<String> lst = ftpComponent.listFilePath("/test");

        List<FtpFileBean> fileBeans = new ArrayList<>();
        for (String fileName : lst) {
            fileBeans.add(new FtpFileBean("d:/", fileName, "/test/", fileName));
        }

        ftpComponent.downloadFiles(fileBeans);

        ftpComponent.closeConnect();
    }

    @Test
    public void testDeleteFile() throws Exception {
        BaseFtpComponent ftpComponent = FtpComponentFactory.getFtpComponent(FtpConstants.FtpConnectEnum.NEXCESS_FTP);

        ftpComponent.openConnect();

        ftpComponent.deleteFile(new FtpFileBean(null, null, "/test/", "testaacc-111.jpg"));

        ftpComponent.closeConnect();
    }

    @Test
    public void testDeleteFiles() throws Exception {
        BaseFtpComponent ftpComponent = FtpComponentFactory.getFtpComponent(FtpConstants.FtpConnectEnum.NEXCESS_FTP);

        ftpComponent.openConnect();

        List<FtpFileBean> fileBeans = new ArrayList<>();
        fileBeans.add(new FtpFileBean(null, null, "/test/", "testaacc-111.jpg"));
        fileBeans.add(new FtpFileBean(null, null, "/test/", "snusa-detail_20.png"));

        ftpComponent.deleteFiles(fileBeans);

        ftpComponent.closeConnect();
    }

}
