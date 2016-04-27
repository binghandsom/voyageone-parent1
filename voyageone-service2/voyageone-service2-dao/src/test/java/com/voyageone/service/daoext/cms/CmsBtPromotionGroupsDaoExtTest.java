package com.voyageone.service.daoext.cms;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;

/**
 * Created by Ethan Shi on 2016/4/26.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context.xml")

public class CmsBtPromotionGroupsDaoExtTest {

    @Autowired
    private CmsBtPromotionGroupsDaoExt cmsBtPromotionGroupsDaoExt;

    @Test
    public void testSelectPromotionModelList() throws Exception {

        HashMap map = new HashMap();
        map.put("start", 1);
        map.put("length", 1);

        cmsBtPromotionGroupsDaoExt.selectPromotionModelList(map);

    }

    @Test
    public void testSelectPromotionModelDetailList() throws Exception {

    }

    @Test
    public void testSelectPromotionModelDetailListCnt() throws Exception {

    }
}