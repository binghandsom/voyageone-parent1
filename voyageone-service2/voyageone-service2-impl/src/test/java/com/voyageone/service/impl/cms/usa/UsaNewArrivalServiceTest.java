package com.voyageone.service.impl.cms.usa;

import com.voyageone.common.util.JacksonUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * Created by james on 2017/7/20.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context-service2.xml")
public class UsaNewArrivalServiceTest {
    @Autowired
    UsaNewArrivalService usaNewArrivalService;
    @Test
    public void getNewArrivalCategory() throws Exception {
        System.out.println(JacksonUtil.bean2Json(usaNewArrivalService.getNewArrivalCategory("shoes","men")));
        System.out.println(JacksonUtil.bean2Json(usaNewArrivalService.getNewArrivalCategory("shoes","Women")));
        System.out.println(JacksonUtil.bean2Json(usaNewArrivalService.getNewArrivalCategory("shoes","Big Kids")));
        System.out.println(JacksonUtil.bean2Json(usaNewArrivalService.getNewArrivalCategory("apparel","men")));
        System.out.println(JacksonUtil.bean2Json(usaNewArrivalService.getNewArrivalCategory("apparel","Women")));
        System.out.println(JacksonUtil.bean2Json(usaNewArrivalService.getNewArrivalCategory("Sunglasses","One Size")));
    }

}