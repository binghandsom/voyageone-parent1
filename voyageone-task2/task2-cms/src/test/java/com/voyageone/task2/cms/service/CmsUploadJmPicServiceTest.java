package com.voyageone.task2.cms.service;

import com.voyageone.components.jumei.Bean.JmImageFileBean;
import com.voyageone.components.jumei.service.JumeiImageFileService;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.HttpUtils;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author aooer 2016/1/26.
 * @version 2.0.0
 * @since 2.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class CmsUploadJmPicServiceTest {

    @Autowired
    CmsUploadJmPicService cmsUploadJmPicService;

    @Autowired
    JumeiImageFileService jumeiImageFileService;

    @Test
    public void testOnStartup() throws Exception {
        List<TaskControlBean> taskControlList = new ArrayList<>();
        cmsUploadJmPicService.onStartup(taskControlList);
    }

    @Test
    public void uploadPic() throws Exception {
        ShopBean shopBean = new ShopBean();
        shopBean.setAppKey("131");
        shopBean.setSessionKey("7e059a48c30c67d2693be14275c2d3be");
        shopBean.setAppSecret("0f9e3437ca010f63f2c4f3a216b7f4bc9698f071");
        shopBean.setApp_url("http://openapi.ext.jumei.com/");

        JmImageFileBean jmImageFileBean = new JmImageFileBean();
        String juUrl = jumeiImageFileService.imageFileUpload(shopBean, convertJmPicToImageFileBean("http://p0.jmstatic.com/global/image/201602/24/1456296871.9148.jpg"));
        System.out.println(juUrl);
    }

    private static JmImageFileBean convertJmPicToImageFileBean(String  url) throws Exception {

        JmImageFileBean jmImageFileBean = new JmImageFileBean();
//            File imageFile=new File(jmPicBean.getOriginUrl());
        int retryCount = 5;
        InputStream inputStream = getImgInputStream(url, retryCount);
        Assert.notNull(inputStream, "inputStream为null，图片流获取失败！" + url);
        jmImageFileBean.setInputStream(inputStream);
        jmImageFileBean.setDirName("015/channel");
        jmImageFileBean.setImgName("Gilt6_4");
        jmImageFileBean.setNeedReplace(true);
        jmImageFileBean.setExtName("jpg");
        return jmImageFileBean;
    }

    private static InputStream getImgInputStream(String url, int retry) throws Exception {
        Exception exception = null;
        if (--retry > 0) {
            try {
                return HttpUtils.getInputStream(url, null);
            } catch (Exception e) {
                exception = e;
                getImgInputStream(url, retry);
            }
        }
        throw new Exception(url+"取得失败");
    }
}