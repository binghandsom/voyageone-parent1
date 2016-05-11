package com.voyageone.service.daoext.cms;

import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.bean.cms.CmsBtPromotionCodesBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Ethan Shi on 2016/5/11.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context.xml")

public class CmsBtPromotionCodesDaoExtTest {

    @Autowired
    private CmsBtPromotionCodesDaoExt cmsBtPromotionCodesDaoExt;

    @Test
    public void testSelectPromotionCodeList() throws Exception {
        HashMap map = new HashMap();
        map.put("promotionId", "7");
        map.put("start", 1);
        map.put("length", 1);
        List<CmsBtPromotionCodesBean> list =  cmsBtPromotionCodesDaoExt.selectPromotionCodeList(map);
        System.out.println(JacksonUtil.bean2Json(list));

    }

    @Test
    public void testSelectPromotionCodeSkuList() throws Exception {

    }

    @Test
    public void testSelectPromotionCodeListCnt() throws Exception {

    }

    @Test
    public void testInsertPromotionCode() throws Exception {

    }

    @Test
    public void testUpdatePromotionCode() throws Exception {

    }

    @Test
    public void testDeletePromotionCode() throws Exception {

    }

    @Test
    public void testDeletePromotionCodeByModelId() throws Exception {

    }

    @Test
    public void testSelectCmsBtPromotionAllCodeByPromotionIdS() throws Exception {

    }
}