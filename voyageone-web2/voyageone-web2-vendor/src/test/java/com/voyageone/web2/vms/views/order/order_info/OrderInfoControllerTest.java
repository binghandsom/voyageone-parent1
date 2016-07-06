package com.voyageone.web2.vms.views.order.order_info;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * orderInfoControllerTest
 * Created by vantis on 16-7-6.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath*:META-INF/context-web2.xml","classpath*:META-INF/context-web2-mvc.xml"})
public class OrderInfoControllerTest {

    @Autowired
    OrderInfoController orderInfoController;

    @Test
    public void init() throws Exception {
        orderInfoController.init();
    }

}