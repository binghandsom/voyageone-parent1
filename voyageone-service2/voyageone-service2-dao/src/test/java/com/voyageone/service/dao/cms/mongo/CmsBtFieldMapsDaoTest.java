package com.voyageone.service.dao.cms.mongo;

import com.voyageone.service.model.cms.mongo.CmsBtFieldMapsModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;

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
public class CmsBtFieldMapsDaoTest {

    @Autowired
    private CmsBtFieldMapsDao fieldMapsDao;

    @Test
    public void selectOne() throws Exception {

        CmsBtFieldMapsModel model = new CmsBtFieldMapsModel();

        model.setCartId(23);
        model.setCategoryType(1);
        model.setCategoryId("测试类目");
        model.setChannelId("010");

        fieldMapsDao.insert(model);

        CmsBtFieldMapsModel modelInDb = fieldMapsDao.selectOne(model.getCartId(), model.getCategoryType(),
                model.getCategoryId(), model.getChannelId());

        assertTrue(modelInDb.getCartId().equals(model.getCartId()));
        assertTrue(modelInDb.getCategoryId().equals(model.getCategoryId()));
        assertTrue(modelInDb.getCategoryType().equals(model.getCategoryType()));

        fieldMapsDao.delete(modelInDb);
    }
}