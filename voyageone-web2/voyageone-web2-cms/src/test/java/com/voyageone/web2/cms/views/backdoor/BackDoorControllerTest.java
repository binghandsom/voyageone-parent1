package com.voyageone.web2.cms.views.backdoor;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * Created by james on 2016/12/5.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration({"classpath*:META-INF/context-web2.xml","classpath*:META-INF/context-web2-mvc.xml"})
public class BackDoorControllerTest {

    @Autowired
    BackDoorController backDoorController;
    @Test
    public void procductPriceUpdate() throws Exception {
        backDoorController.procductPriceUpdate("028",27);
    }

    @Test
    public void addNewGroup() {
        backDoorController.addNewGroup("024", 0, null, false);
    }

    @Test
    public void updateJmPromotionMallIdByPromotionId() {
        backDoorController.updateJmPromotionMallIdByPromotionId("001", 1860, "");
    }

    @Test
    public void updateJmPromotionMallIdByPromotionId_withCode() {
        backDoorController.updateJmPromotionMallIdByPromotionId("001", 1860, "aq8014");
    }

}