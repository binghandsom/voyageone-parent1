package com.voyageone.service.dao.cms.mongo;

import com.voyageone.service.model.cms.mongo.CmsBtPlatformMappingModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * 对平台类目的属性匹配进行操作测试
 * <p>
 * Created by jonas on 8/14/16.
 *
 * @author jonas
 * @version 2.4.0
 * @since 2.4.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context.xml")
public class CmsBtPlatformMappingDaoTest {

    @Autowired
    private CmsBtPlatformMappingDao platformMappingDao;

    @Test
    public void selectOne() throws Exception {

        CmsBtPlatformMappingModel model = new CmsBtPlatformMappingModel();

        model.setCartId(23);
        model.setCategoryType(1);
        model.setCategoryPath("测试类目");
        model.setChannelId("010");

        platformMappingDao.insert(model);

        CmsBtPlatformMappingModel modelInDb = platformMappingDao.selectOne(model.getCartId(), model.getCategoryType(),
                model.getCategoryPath(), model.getChannelId());

        assertTrue(modelInDb.getCartId().equals(model.getCartId()));
        assertTrue(modelInDb.getCategoryPath().equals(model.getCategoryPath()));
        assertTrue(modelInDb.getCategoryType().equals(model.getCategoryType()));

        platformMappingDao.delete(modelInDb);
    }
}