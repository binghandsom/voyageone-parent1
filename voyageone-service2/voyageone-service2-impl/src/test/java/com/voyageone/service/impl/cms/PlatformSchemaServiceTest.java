package com.voyageone.service.impl.cms;

import com.voyageone.common.masterdate.schema.field.Field;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Map;

/**
 * @author james.li on 2016/7/11.
 * @version 2.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context-service2.xml")
public class PlatformSchemaServiceTest {

    @Autowired
    PlatformSchemaService platformSchemaService;

    @Test
    public void testGetSchema() throws Exception {

        Map<String, List<Field>> map = platformSchemaService.getFieldForProductImage("50016235","018",23, "cn", null, null);

        System.out.println("break point");
    }
}