package com.voyageone.components.jumei.service;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.HttpUtils;
import com.voyageone.components.jumei.bean.JmImageFileBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.InputStream;
import java.util.UUID;

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
        shopBean.setAppKey("110");
        shopBean.setAppSecret("6dbc6df2c67634192e01c2311cab4372575eebeb");
        shopBean.setSessionKey("f06e250dd5d30ab9db3e24c362438c69");
        shopBean.setApp_url("http://openapi.ext.jmrd.com:8823/");


        // 读取图片
        InputStream inputStream = getImgInputStream("http://image.voyageone.com.cn/cms/store/010/20160620214038.jpg", 3);

        //上传图片
        JmImageFileBean fileBean = new JmImageFileBean();
        //用UUID命名
        fileBean.setImgName(UUID.randomUUID().toString());
        fileBean.setInputStream(inputStream);
        fileBean.setNeedReplace(false);
        fileBean.setDirName("010");
        fileBean.setExtName("jpg");
        String jmPicUrl = imageFileService.imageFileUpload(shopBean, fileBean);

        System.out.println(jmPicUrl);
    }


    /**
     * 获取网络图片流，遇错重试
     *
     * @param url   imgUrl
     * @param retry retrycount
     * @return inputStream / throw Exception
     */
    public static InputStream getImgInputStream(String url, int retry) throws BusinessException {
        if (--retry > 0) {
            try {
                return HttpUtils.getInputStream(url, null);
            } catch (Exception e) {
                getImgInputStream(url, retry);
            }
        }
        throw new BusinessException("通过URL取得图片失败. url:" + url);
    }
}
