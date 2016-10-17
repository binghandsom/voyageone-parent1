package com.voyageone.service.impl.cms;

import com.voyageone.service.model.cms.mongo.jm.promotion.CmsBtJmBayWindowModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by james on 2016/10/17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context-service2.xml")
public class CmsBtJmBayWindowServiceTest {
    @Autowired
    CmsBtJmBayWindowService cmsBtJmBayWindowService;
    @Test
    public void insert() throws Exception {
        CmsBtJmBayWindowModel cmsBtJmBayWindowModel = new CmsBtJmBayWindowModel();
        List<CmsBtJmBayWindowModel.BayWindow> bayWindows = new ArrayList<>();
        CmsBtJmBayWindowModel.BayWindow bayWindow = new CmsBtJmBayWindowModel.BayWindow();
        bayWindow.setLink("http://aasda.jpg");
        bayWindow.setName("aaa");
        bayWindow.setOrder(2);
        bayWindows.add(bayWindow);
        bayWindow = new CmsBtJmBayWindowModel.BayWindow();
        bayWindow.setLink("http://ccccc.jpg");
        bayWindow.setName("ccc");
        bayWindow.setOrder(1);
        bayWindows.add(bayWindow);
        cmsBtJmBayWindowModel.setCartId(23);
        cmsBtJmBayWindowModel.setJmPromotionId(111);
        cmsBtJmBayWindowModel.setBayWindows(bayWindows);
        cmsBtJmBayWindowService.insert(cmsBtJmBayWindowModel);
    }

}