package com.voyageone.cms.service.dao.mongodb;


import com.mongodb.WriteResult;
import com.voyageone.cms.service.CmsProductService;
import com.voyageone.cms.service.model.*;
import net.minidev.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class CmsProductDaoTest {
    @Autowired
    CmsBtProductDao cmsBtProductDao;

    @Test
    public void testSelectProductById() throws Exception {
        long start = System.currentTimeMillis();
        CmsBtProductModel model = cmsBtProductDao.selectById("565d97127b69bf04fdd12450", "100");
        System.out.println("time total:=" + (System.currentTimeMillis() - start));
        System.out.println(model.toString());
    }

    @Test
    public void testSelectProductByCode() throws Exception {
        long start = System.currentTimeMillis();
        CmsBtProductModel model = cmsBtProductDao.selectProductByCode("100", "100011");
        System.out.println("time total:=" + (System.currentTimeMillis() - start));
        System.out.println(model.toString());
    }

    @Test
    public void testSelectProductLikeCatIdPath() throws Exception {
        long start = System.currentTimeMillis();
        List<CmsBtProductModel> modelList = cmsBtProductDao.selectLeftLikeCatIdPath("100", "-100-10000-17");
        System.out.println("time total:=" + (System.currentTimeMillis() - start) + "; size:=" + modelList.size());
        for (CmsBtProductModel model : modelList) {
            //System.out.println(model.toString());
        }
    }

    @Test
    public void selectAllReturnCursor() {
        long start = System.currentTimeMillis();
        Iterator<CmsBtProductModel> iterator = cmsBtProductDao.selectCursorAll("100");
        System.out.println("time total:=" + (System.currentTimeMillis() - start));
        int count = 0;
        while(iterator.hasNext()) {
            CmsBtProductModel model = iterator.next();
            System.out.println(model.toString());
            count++;
            if (count>10) {
                break;
            }
        }
    }



}