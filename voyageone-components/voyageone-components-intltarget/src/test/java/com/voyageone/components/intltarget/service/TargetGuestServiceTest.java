package com.voyageone.components.intltarget.service;

import com.voyageone.common.util.JacksonUtil;
import com.voyageone.components.intltarget.bean.guest.TargetGuestAccount;
import com.voyageone.components.intltarget.bean.guest.TargetGuestAccountRequest;
import com.voyageone.components.intltarget.bean.guest.TargetGuestShippingAddress;
import com.voyageone.components.intltarget.bean.guest.TargetGuestShippingAddressRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.UUID;

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

    @Test
    public void testCreateGuestAccount() throws Exception {

        TargetGuestAccountRequest request=new TargetGuestAccountRequest();
        request.setFirstName("aooer");
        request.setMiddleName("a");
        request.setLastName("aooer");
        request.setLogonId(UUID.randomUUID()+"@voyageone.com");
        request.setLogonPassword("voyageone0");
        request.setLogonPasswordVerify("voyageone0");
        request.setSendMeEmail("false");
        TargetGuestAccount account=targetGuestService.createGuestAccount(request);
        System.out.println(JacksonUtil.bean2Json(account));
    }
}