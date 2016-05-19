package com.voyageone.service.daoext.cms;

import com.voyageone.common.util.JacksonUtil;
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

public class CmsBtPromotionDaoExtTest {

    @Autowired
    private CmsBtPromotionDaoExt cmsBtPromotionDaoExt;

    @Test
    public void testSelectByCondition() throws Exception {

        HashMap map = new HashMap();

        map.put("channelId", 1);

        System.out.println(JacksonUtil.bean2Json(cmsBtPromotionDaoExt.selectByCondition(map)));
    }

    @Test
    public void testSelectById() throws Exception {
        HashMap map = new HashMap();

        map.put("channelId", 1);

        System.out.println(JacksonUtil.bean2Json(cmsBtPromotionDaoExt.selectByCondition(map)));
    }


    @Test
    public void testUpdate() throws Exception {

    }

    @Test
    public void testInsert() throws Exception {

    }

    @Test
    public void testDeleteById() throws Exception {

    }

    @Test
    public void testSelectPromotionIDByCartId() throws Exception {

    }

    @Test
    public void testSelectPromotionHistory() throws Exception {

    }

    @Test
    public void testSelectPromotionHistoryCnt() throws Exception {

    }
}