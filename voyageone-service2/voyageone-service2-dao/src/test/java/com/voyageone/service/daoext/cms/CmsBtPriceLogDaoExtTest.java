package com.voyageone.service.daoext.cms;

import com.voyageone.service.model.cms.CmsBtPriceLogModel_Mysql;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Ethan Shi on 2016/4/26.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context.xml")
public class CmsBtPriceLogDaoExtTest {


    @Autowired
    private CmsBtPriceLogDaoExt cmsBtPriceLogDaoExt;

    @Test
    public void testSelectPriceLogByCode() throws Exception {

        HashMap map = new HashMap();
        map.put("code", "49896");
        map.put("offset", 1);
        map.put("rows", 1);

        cmsBtPriceLogDaoExt.selectPriceLogByCode(map);

    }

    @Test
    public void testSelectPriceLogByCodeCnt() throws Exception {

    }



    @Test
    public void testInsertCmsBtPriceLogList() throws Exception {
        List<CmsBtPriceLogModel_Mysql> list  =  new ArrayList<CmsBtPriceLogModel_Mysql>();
        CmsBtPriceLogModel_Mysql cmsBtPriceLogModel = new CmsBtPriceLogModel_Mysql();
        cmsBtPriceLogModel.setChannelId("1");
        cmsBtPriceLogModel.setProductId(1);
        cmsBtPriceLogModel.setCode("1");
        cmsBtPriceLogModel.setSku("1");
        cmsBtPriceLogModel.setSalePrice(555d);
        cmsBtPriceLogModel.setMsrpPrice(11111d);
        cmsBtPriceLogModel.setRetailPrice(2222d);
        cmsBtPriceLogModel.setClientMsrpPrice(333d);
        cmsBtPriceLogModel.setClientRetailPrice(44d);
        cmsBtPriceLogModel.setClientNetPrice(4444d);
        cmsBtPriceLogModel.setComment("5555555");
        cmsBtPriceLogModel.setCreated(new Date());
        cmsBtPriceLogModel.setCreater("testuser");
        cmsBtPriceLogModel.setModified(new Date());
        cmsBtPriceLogModel.setModifier("testuser");

        list.add(cmsBtPriceLogModel);
        cmsBtPriceLogDaoExt.insertCmsBtPriceLogList(list);

    }
}