package com.voyageone.service.impl.cms.feed;

import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.dao.cms.mongo.CmsBtCAdProductDao;
import com.voyageone.service.model.cms.mongo.CmsBtCAdProudctModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.List;

/**
 * @author james.li on 2016/9/12.
 * @version 2.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class CmsBtCAdProductServiceTest {

    @Autowired
    CmsBtCAdProductDao cmsBtCAdProductDao;
    @Test
    public void testUpdateQuantityPrice() throws Exception {

    }

    @Test
    public void testUpdateProduct() throws Exception {
        List<String> skus = Arrays.asList("REBEL X-WING","LIGHTSABER");
        List<CmsBtCAdProudctModel> aa = cmsBtCAdProductDao.getProduct("996", skus);
        System.out.println(JacksonUtil.bean2Json(aa));
    }
}