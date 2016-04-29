package com.voyageone.service.impl.cms.imagecreate;

import org.junit.Test;
import org.junit.runner.RunWith;
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

    @Test
    public void test()
    {
        //ImagePathCache.set(3443,"testvalue");
        String result=ImagePathCache.get(323L);
        //Assert.notNull();
    }
}