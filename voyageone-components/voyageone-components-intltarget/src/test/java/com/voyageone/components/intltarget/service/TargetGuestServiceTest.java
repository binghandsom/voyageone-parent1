package com.voyageone.components.intltarget.service;

import com.voyageone.common.util.JacksonUtil;
import com.voyageone.components.intltarget.bean.TargetGuestShippingAddress;
import com.voyageone.components.intltarget.bean.TargetGuestShippingAddressRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;

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
    public void testGetGuestShippingAddress() throws Exception {

        TargetGuestShippingAddressRequest request = new TargetGuestShippingAddressRequest();
        request.setFirstName("Mary");
        request.setLastName("Smith");
        request.setMiddleName("K");
        request.setAddressType("Shipping");
        request.setAddressLine(Arrays.asList("416 Water st.", "Water St"));
        request.setCity("New york");
        request.setState("NY");
        request.setZipCode("10002-7811");
        request.setPhoneType("Home");
        request.setPhone("8112142326");
        request.setSaveAsDefault("N");
        request.setSkipAddressValidation("N");
        TargetGuestShippingAddress add = targetGuestService.getGuestShippingAddress(request);
        System.out.println(JacksonUtil.bean2Json(add));
    }

}