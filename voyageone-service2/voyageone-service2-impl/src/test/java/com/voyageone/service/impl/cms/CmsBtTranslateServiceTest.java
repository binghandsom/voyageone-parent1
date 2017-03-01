package com.voyageone.service.impl.cms;

import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.model.cms.mongo.CmsBtTranslateModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.*;

/**
 *
 * Created by james on 2017/3/1.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context-service2.xml")
public class CmsBtTranslateServiceTest {
    @Autowired
    CmsBtTranslateService cmsBtTranslateService;
    @Test
    public void select() throws Exception {
        List<CmsBtTranslateModel> ret= cmsBtTranslateService.select("010",3,"MetalStamp",null, 1,10);
        System.out.println(JacksonUtil.bean2Json(ret));

        ret.get(0).setValueCn(ret.get(0).getValueCn()+"+++");
        cmsBtTranslateService.insertOrUpdate(ret.get(0));
    }

}