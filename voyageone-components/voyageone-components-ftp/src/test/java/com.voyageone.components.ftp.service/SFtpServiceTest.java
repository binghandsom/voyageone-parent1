package com.voyageone.components.ftp.service;

import com.voyageone.components.ftp.FtpBeanEnum;
import com.voyageone.components.ftp.FtpBeanFactory;
import com.voyageone.components.ftp.bean.FtpDirectoryBean;
import com.voyageone.components.ftp.bean.FtpSubFileBean;
import com.voyageone.components.ftp.bean.FtpFilesBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

/**
 * @author chuanyu.liang 2016/5/12.
 * @version 2.0.0
 * @since 2.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context.xml")
public class SFtpServiceTest {

    @Autowired
    private SFtpService sFtpService;

    @Test
    public void testStoreFiles() throws Exception {
        FtpFilesBean ftpBean = FtpBeanFactory.getFtpFilesBean(FtpBeanEnum.VO_IMAGE_CMS);

        ftpBean.addFileBean(new FtpSubFileBean("d:/", "testaacc-111.jpg", "/test"));
        ftpBean.addFileBean(new FtpSubFileBean("d:/", "snusa-detail_20.png", "/test"));

        sFtpService.uploadFiles(ftpBean);
    }

    @Test
    public void testStoreFilesStream() throws Exception {
        FtpFilesBean ftpBean = FtpBeanFactory.getFtpFilesBean(FtpBeanEnum.VO_IMAGE_CMS);

        ftpBean.addFileBean(new FtpSubFileBean(new FileInputStream(new File("d:/testaacc-111.jpg")), "/test", "testaacc-111.jpg"));
        ftpBean.addFileBean(new FtpSubFileBean(new FileInputStream(new File("d:/snusa-detail_20.png")), "/test", "snusa-detail_20.png"));

        sFtpService.uploadFiles(ftpBean);
    }


    @Test
    public void testDownloadFiles() throws Exception {
        FtpFilesBean ftpBean = FtpBeanFactory.getFtpFilesBean(FtpBeanEnum.VO_IMAGE_CMS);

        ftpBean.addFileBean(new FtpSubFileBean("d:/", "testaacc-112.jpg", "/test", "testaacc-111.jpg"));
        ftpBean.addFileBean(new FtpSubFileBean("d:/", "snusa-detail_21.png", "/test", "snusa-detail_20.png"));

        sFtpService.downloadFiles(ftpBean);
    }

    @Test
    public void testListFiles() throws Exception {
        FtpDirectoryBean ftpBean = FtpBeanFactory.getFtpDirectoryBean(FtpBeanEnum.VO_IMAGE_CMS);

        ftpBean.setRemotePath("/test");

        List<String> lst = sFtpService.listFiles(ftpBean);
        for (String fileName : lst) {
            System.out.println(fileName);
        }
    }

    @Test
    public void testDownloadFileDir() throws Exception {
        FtpDirectoryBean ftpBean = FtpBeanFactory.getFtpDirectoryBean(FtpBeanEnum.VO_IMAGE_CMS);
        ftpBean.setRemotePath("/test");
        ftpBean.setLocalPath("d:/");

        sFtpService.downloadDirector(ftpBean);
    }

    @Test
    public void testDeleteFiles() throws Exception {
        FtpFilesBean ftpBean = FtpBeanFactory.getFtpFilesBean(FtpBeanEnum.VO_IMAGE_CMS);

        FtpSubFileBean ftpFileBean = new FtpSubFileBean();
        ftpFileBean.setRemotePath("/test");
        ftpFileBean.setRemoteFilename("testaacc-111.jpg");
        ftpBean.addFileBean(ftpFileBean);

        ftpFileBean = new FtpSubFileBean();
        ftpFileBean.setRemotePath("/test");
        ftpFileBean.setRemoteFilename("snusa-detail_20.png");
        ftpBean.addFileBean(ftpFileBean);

        sFtpService.deleteFiles(ftpBean);
    }


}
