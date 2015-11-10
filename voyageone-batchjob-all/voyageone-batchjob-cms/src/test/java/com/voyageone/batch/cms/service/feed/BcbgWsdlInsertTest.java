package com.voyageone.batch.cms.service.feed;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 插入部分的单元测试
 * Created by Jonas on 11/9/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class BcbgWsdlInsertTest {
    @Autowired
    private BcbgWsdlInsert bcbgWsdlInsert;

    @Before
    public void setUp() throws Exception {
        BcbgWsdlConstants.init();
    }

    @Test
    public void testPostNewProduct() throws Exception {
        bcbgWsdlInsert.postNewProduct();
    }
}