package com.voyageone.batch.cms.service.feed;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 更新商品的单元测试
 * Created by Jonas on 11/9/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class BcbgWsdlUpdateTest {
    @Autowired
    private BcbgWsdlUpdate bcbgWsdlUpdate;

    @Before
    public void setUp() throws Exception {
        BcbgWsdlConstants.init();
    }

    @Test
    public void testPostUpdatedProduct() throws Exception {
        bcbgWsdlUpdate.postUpdatedProduct();
    }
}