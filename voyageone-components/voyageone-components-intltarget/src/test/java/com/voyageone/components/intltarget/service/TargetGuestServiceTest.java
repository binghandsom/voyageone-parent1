package com.voyageone.components.intltarget.service;

import com.voyageone.common.util.JacksonUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * @author aooer 2016/5/4.
 * @version 2.0.0
 * @since 2.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context.xml")
public class TargetGuestServiceTest {

    @Autowired
    private TargetGuestService targetGuestService;

    @Test
    public void testV3Auth() throws Exception {
        System.out.println(JacksonUtil.bean2Json(targetGuestService.v3Auth()));
    }
}