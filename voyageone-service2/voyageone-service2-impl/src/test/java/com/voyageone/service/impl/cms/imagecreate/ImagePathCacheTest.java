package com.voyageone.service.impl.cms.imagecreate;

import com.voyageone.base.dao.mongodb.JomgoQuery;
import com.voyageone.service.dao.cms.mongo.CmsBtImageTemplateDao;
import com.voyageone.service.model.cms.mongo.channel.CmsBtImageTemplateModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import static org.junit.Assert.*;

/**
 * Created by dell on 2016/4/29.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class ImagePathCacheTest {

    @Autowired
    ImagePathCache imagePathCache;

    @Test
    public void test() {
        //ImagePathCache.set(3443,"testvalue");
        String result = imagePathCache.get(323L);
        //Assert.notNull();
    }

    @Autowired
    private CmsBtImageTemplateDao cmsBtImageTemplateDao;

    @Test
    public void testGetTemplate() {
        CmsBtImageTemplateModel model = cmsBtImageTemplateDao.selectByTemplateId(50);
        model = cmsBtImageTemplateDao.selectByTemplateId(50);
        model = cmsBtImageTemplateDao.selectByTemplateId(50);
        System.out.println(model);
    }
}