package com.voyageone.task2.cms.service;

import com.voyageone.service.model.cms.CmsBtImagesModel;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import com.voyageone.task2.cms.service.CmsImagePostScene7Service;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.fluent.Request;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author james.li on 2016/1/6.
 * @version 2.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class CmsImagePostScene7ServiceTest {

    @Autowired
    private CmsImagePostScene7Service cmsImagePostScene7Service;

    @Test
    public void testOnStartup() throws Exception {
        List<TaskControlBean> taskControlList = new ArrayList<>();
        TaskControlBean taskControlBean = new TaskControlBean();
        taskControlBean.setCfg_name("order_channel_id");
        taskControlBean.setCfg_val1("017");
        taskControlList.add(taskControlBean);
        cmsImagePostScene7Service.onStartup(taskControlList);
    }

    @Test
    public void testOnStartup1() throws Exception {

    }

    @Test
    public void testGetAndSendImage() {
        String orderChannelId = "045";
        List<CmsBtImagesModel> imageUrlList = new ArrayList<>();

        CmsBtImagesModel model = new CmsBtImagesModel();
        model.setOriginalUrl("http://d3d71ba2asa5oz.cloudfront.net/12016207/images/bb0189-parent__1.jpg");
        model.setImgName("045-045-844656-006-PARENT-CoolGreyStadiumGreen-1");
        imageUrlList.add(model);

        List<CmsBtImagesModel> successImageUrlList = new ArrayList<>();
        List<CmsBtImagesModel> urlErrorList = new ArrayList<>();

//        try (InputStream stream = Request.Get(model.getOriginalUrl()).execute().returnContent().asStream()) {
            try (InputStream stream = new URL(model.getOriginalUrl()).openStream()) {
                FileOutputStream aa = new FileOutputStream("h://cc.jpg");
                IOUtils.copy(stream,aa);
                aa.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        int threadNo = 0;

//        cmsImagePostScene7Service.getAndSendImage(orderChannelId, imageUrlList, successImageUrlList, urlErrorList, threadNo);
    }

    @Autowired
    ImagePostScene7Service imagePostScene7Service;
    @Test
    public void testGetAndSendImage1() {
        String orderChannelId = "024";
        List<Map<String, String>> imageUrlList = new ArrayList<>();

        Map<String, String> model = new HashMap<>();
        model.put("image_url", "http://p0.jmstatic.com/partner_product/2015new/09/23/11/38/14429794817080.jpg");
        model.put("channel_id", "024");
        imageUrlList.add(model);

        List<String> successImageUrlList = new ArrayList<>();
        List<Map<String, String>> urlErrorList = new ArrayList<>();

        int threadNo = 0;

        imagePostScene7Service.getAndSendImage(orderChannelId, imageUrlList, successImageUrlList, urlErrorList, threadNo);
    }
}