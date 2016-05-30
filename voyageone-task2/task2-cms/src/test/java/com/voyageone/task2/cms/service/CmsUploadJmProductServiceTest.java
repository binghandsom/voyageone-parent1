package com.voyageone.task2.cms.service;

import com.voyageone.components.jumei.bean.JmGetProductInfoRes;
import com.voyageone.components.jumei.service.JumeiProductService;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author james.li on 2016/1/26.
 * @version 2.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class CmsUploadJmProductServiceTest {

    @Autowired
    private CmsUploadJmProductService cmsUploadJmProductService;

    @Autowired
    private JumeiProductService jumeiProductService;

    @Test
    public void testOnStartup() throws Exception {
        List<TaskControlBean> taskControlList = new ArrayList<>();
        cmsUploadJmProductService.onStartup(taskControlList);
    }

    @Test
    public void testGetTime() throws Exception {

        Long a = CmsUploadJmProductService.getTime("2016-03-10 10:00:00");
        System.out.println(a);
        ShopBean shopBean = new ShopBean();
        shopBean.setAppKey("131");
        shopBean.setSessionKey("7e059a48c30c67d2693be14275c2d3be");
        shopBean.setAppSecret("0f9e3437ca010f63f2c4f3a216b7f4bc9698f071");
        shopBean.setApp_url("http://openapi.ext.jumei.com/");
        JmGetProductInfoRes jmGetProductInfoRes = jumeiProductService.getProductByName(shopBean, "D G迷人小号皮质圆形挎包 1124229756");
        return;
    }

    @Test
    public void testGetProductInfo() throws Exception {
        ShopBean shopBean = new ShopBean();
        shopBean.setAppKey("131");
        shopBean.setSessionKey("7e059a48c30c67d2693be14275c2d3be");
        shopBean.setAppSecret("0f9e3437ca010f63f2c4f3a216b7f4bc9698f071");
        shopBean.setApp_url("http://openapi.ext.jumei.com/");
        String[] productList = {"2382646",
                "2383491",
                "2382859",
                "2382969",
                "2382868",
                "2382966",
                "2382867",
                "2382861",
                "2382863",
                "2382974",
                "2382965",
                "2382871",
                "2382860",
                "2385006",
                "2383903",
                "2383905",
                "2383906",
                "2383910",
                "2383913",
                "2383914",
                "2383915",
                "2384829",
                "2383929",
                "2383930",
                "2383932",
                "2383938",
                "2383939",
                "2383940",
                "2383944",
                "2383948",
                "2383949",
                "2383952",
                "2383953",
                "2383955",
                "2385276",
                "2385491",
                "2385493",
                "2385492",
                "2385275",
                "2385525",
                "2385530",
                "2385515",
                "2385521",
                "2385498",
                "2385509",
                "2385504",
                "2385506",
                "2385502",
                "2385511"};

        FileWriter fw = new FileWriter("f:\\jumei_hash_id4.csv");
        PrintWriter pw = new PrintWriter(fw);

        try {
            for (int i = 0; i < productList.length; i++) {
                JmGetProductInfoRes jmGetProductInfoRes = jumeiProductService.getProductById(shopBean, productList[i]);
                pw.println((i + 1) + "," + jmGetProductInfoRes.getProduct_id() + "," + jmGetProductInfoRes.getHash_ids());
                if (i % 10 == 0) {
                    pw.flush();
                }
            }
        } catch (Exception e) {

        } finally {
            pw.close();
            fw.close();
        }

    }
}