package com.voyageone.batch.cms.service;

import com.voyageone.batch.core.modelbean.TaskControlBean;
import com.voyageone.common.components.jumei.Bean.JmGetProductInfoRes;
import com.voyageone.common.components.jumei.JumeiProductService;
import com.voyageone.common.configs.Enums.CartEnums;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.common.configs.ShopConfigs;
import com.voyageone.common.configs.beans.ShopBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author james.li on 2016/1/26.
 * @version 2.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class CmsUploadJmProductServiceTest {

    @Autowired
    CmsUploadJmProductService cmsUploadJmProductService;

    @Autowired
    JumeiProductService jumeiProductService;
    @Test
    public void testOnStartup() throws Exception {
        List<TaskControlBean> taskControlList = new ArrayList<>();
        cmsUploadJmProductService.onStartup(taskControlList);
    }

    @Test
    public void testGetTime() throws Exception {

        Long a=CmsUploadJmProductService.getTime("2016-03-10 10:00:00");
        System.out.println(a);
        ShopBean shopBean = new ShopBean();
        shopBean.setAppKey("131");
        shopBean.setSessionKey("7e059a48c30c67d2693be14275c2d3be");
        shopBean.setAppSecret("0f9e3437ca010f63f2c4f3a216b7f4bc9698f071");
        shopBean.setApp_url("http://openapi.ext.jumei.com/");
        JmGetProductInfoRes jmGetProductInfoRes = jumeiProductService.getProductByName(shopBean, "D G迷人小号皮质圆形挎包 1124229756");
        return ;
    }

    @Test
    public void testGetProductInfo() throws Exception {
        ShopBean shopBean = new ShopBean();
        shopBean.setAppKey("131");
        shopBean.setSessionKey("7e059a48c30c67d2693be14275c2d3be");
        shopBean.setAppSecret("0f9e3437ca010f63f2c4f3a216b7f4bc9698f071");
        shopBean.setApp_url("http://openapi.ext.jumei.com/");
        String[] productList= {"2364636",
                "2371378",
                "2372326",
                "2372327",
                "2372335",
                "2372337",
                "2372339",
                "2372342",
                "2372344",
                "2382646",
                "2382859",
                "2382860",
                "2382861",
                "2382863",
                "2382867",
                "2382868",
                "2382871",
                "2382969",
                "2382965",
                "2382966",
                "2382974",
                "2383903",
                "2383905",
                "2383906",
                "2383910",
                "2383913",
                "2383914",
                "2383915",
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
                "2383955"};

        FileWriter fw=new FileWriter("f:\\jumei_hash_id3.csv");
        PrintWriter pw=new PrintWriter(fw);

        try {
            for(int i=0;i<productList.length;i++){
                JmGetProductInfoRes jmGetProductInfoRes = jumeiProductService.getProductById(shopBean, productList[i]);
                pw.println((i+1)+","+jmGetProductInfoRes.getProduct_id() + "," + jmGetProductInfoRes.getHash_ids());
                if(i%10 == 0){
                    pw.flush();
                }
            }
        }catch (Exception e){

        }finally {
            pw.close();
            fw.close();
        }

    }
}