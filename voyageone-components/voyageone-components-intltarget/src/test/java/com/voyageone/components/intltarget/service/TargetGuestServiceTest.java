package com.voyageone.components.intltarget.service;

import com.voyageone.components.intltarget.bean.TargetGuestContact;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author aooer 2016/4/8.
 * @version 2.0.0
 * @since 2.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context.xml")
public class TargetGuestServiceTest {

    @Autowired
    private TargetGuestService targetGuestService;

    @Test
    public void testGetGuestContactAddress() throws Exception {
        TargetGuestContact contact=targetGuestService.getGuestContactAddress();
        System.out.println(contact);
    }
}