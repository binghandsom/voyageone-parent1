package com.voyageone.service.impl.cms.product;

import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * @author james.li on 2016/7/7.
 * @version 2.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context-service2.xml")
public class ProductSkuServiceTest {

    @Autowired
    ProductSkuService productSkuService;
}