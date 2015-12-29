package com.voyageone.web2.cms.views.promotion;

import com.voyageone.web2.cms.bean.CmsPromotionProductPriceBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author james.li on 2015/12/16.
 * @version 2.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:META-INF/context-web2.xml")
public class CmsPromotionFileServiceTest {

    @Autowired
    CmsPromotionFileService cmsPromotionFileService;

    @Test
    public void testCodeFile() throws Exception {

                        Map para = new HashMap();
                        para.put("cartId", "21");
                        para.put("channelId", "100");

//        cmsPromotionFileService.getModelExcelFile(para);
        cmsPromotionFileService.getCodeExcelFile(para);
//        cmsPromotionFileService.getSkuExcelFile(para);


//        for (int i = 0; i < 5; i++) {
//
//            System.out.println("i = " + i);
//
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    try {
//                        Map para = new HashMap();
//                        para.put("cartId", "21");
//                        para.put("channelId", "100");
//
//                        cmsPromotionFileService.getSkuExcelFile(para);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        System.out.print(e.getMessage());
//                    }
//                }
//            }).start();
//        }
//
//        wait(500000);


//        List<Runnable> threads = new ArrayList<>();
//        for (int i = 0; i < 5; i++) {
//                threads.add(
//                        new Runnable() {
//                            @Override
//                            public void run() {
//                                try {
//                                    Map para = new HashMap();
//                                    para.put("cartId", "21");
//                                    para.put("channelId", "100");
//
//                                    cmsPromotionFileService.getSkuExcelFile(para);
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                    System.out.print(e.getMessage());
//                                }
//                            }
//                        }
//                );
//        }
//
//        ExecutorService pool = Executors.newFixedThreadPool(5);
//
//        for (int i = 0; i < threads.size(); i++) {
//            pool.execute(threads.get(i));
//        }
//
//        pool.shutdown();
//
//        // 等待子线程结束，再继续执行下面的代码
//        pool.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
    }
}