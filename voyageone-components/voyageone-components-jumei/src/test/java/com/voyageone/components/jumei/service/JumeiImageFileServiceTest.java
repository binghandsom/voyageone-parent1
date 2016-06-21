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
    String Client_id="110";
    String Client_key="f06e250dd5d30ab9db3e24c362438c69";
    String Sign="6dbc6df2c67634192e01c2311cab4372575eebeb";
    String url="http://openapi.ext.jmrd.com:8823";
    @Autowired
    private JumeiImageFileService imageFileService;

    @Test
    public void testGet() throws Exception {
        ShopBean shopBean = new ShopBean();
        shopBean.setAppKey(Client_id);
        shopBean.setAppSecret(Sign);
        shopBean.setSessionKey(Client_key);
        shopBean.setApp_url(url);

        JmImageFileBean fileBean = new JmImageFileBean();
        fileBean.setDirName("test01");
        fileBean.setImgName("test019");
        fileBean.setNeedReplace(true);
        File file  = new File("d:/jmtest.jpg");
        fileBean.setFile(file);

        String url = imageFileService.imageFileUpload(shopBean, fileBean);

        System.out.println(url);
    }
}
