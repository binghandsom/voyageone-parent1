package com.voyageone.task2.cms.service;

import com.voyageone.common.configs.Shops;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Created by Ethan Shi on 2016/7/19.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:context-cms-test.xml")

public class RefreshJMSkuServiceTest {

    @Autowired
    CmsBtProductDao cmsBtProductDao;

    @Autowired
    RefreshJMSkuService refreshJMSkuService;

    @Test
    public void testChangeSkuCode() throws Exception {

        String fileName = "D://11.txt";

        File file = new File(fileName);

        List<String> done = FileUtils.readLines(file);

        HashSet<String> finished = new HashSet<>();
        finished.addAll(done);

        ShopBean shop = Shops.getShop("023", 27);

        String queryStr = "{'platforms.P27.pNumIId': {'$ne': ''}}";

        List<CmsBtProductModel> list = cmsBtProductDao.select(queryStr, "023");

        list = list.stream().filter(w -> !StringUtils.isNullOrBlank2(w.getPlatform(27).getpNumIId())).collect(Collectors.toList());

        System.out.println("共计:" + list.size());
        while (finished.size() < list.size()) {

            list.parallelStream().forEach(model -> {
                try {
                    String hashId = model.getPlatform(27).getpNumIId();
                    if (!finished.contains(hashId)) {
                        hashId = refreshJMSkuService.changeSkuCode(shop, model);
                        finished.add(hashId);
                        FileUtils.writeLines(file, finished);
                        System.out.println("完成数:" + finished.size());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

//            for (CmsBtProductModel model : list) {
//                try {
//                    String hashId = model.getPlatform(27).getpNumIId();
//                    if (!finished.contains(hashId)) {
//                        hashId = refreshJMSkuService.changeSkuCode(shop, model);
//                        finished.add(hashId);
//                        FileUtils.writeLines(file, finished);
//                        System.out.println("完成数:" + finished.size());
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
            System.out.println("------------------------------------------------");
            System.out.println("本轮完成数:" + finished.size());
        }


    }
}