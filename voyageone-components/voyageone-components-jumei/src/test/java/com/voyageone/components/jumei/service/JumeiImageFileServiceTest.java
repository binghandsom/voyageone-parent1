package com.voyageone.components.jumei.service;

import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.components.jumei.bean.JmImageFileBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;

/**
 * Created by DELL on 2016/1/26.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context.xml")
public class JumeiImageFileServiceTest {

    @Autowired
    private JumeiImageFileService imageFileService;

    @Test
    public void testGet() throws Exception {
        ShopBean shopBean = new ShopBean();
        shopBean.setAppKey("72");
        shopBean.setAppSecret("62cc742a25d3ec18ecee9dd5bcc724ccfb2844ac");
        shopBean.setSessionKey("e5f9d143815a520726576040460bd67f");
        shopBean.setApp_url("http://182.138.102.82:8868/");

        JmImageFileBean fileBean = new JmImageFileBean();
        fileBean.setDirName("test01");
        fileBean.setImgName("test019");
        fileBean.setNeedReplace(true);
        File file  = new File("c:/1453180056.8779.jpg");
        fileBean.setFile(file);

        String url = imageFileService.imageFileUpload(shopBean, fileBean);

        System.out.println(url);
    }
}
