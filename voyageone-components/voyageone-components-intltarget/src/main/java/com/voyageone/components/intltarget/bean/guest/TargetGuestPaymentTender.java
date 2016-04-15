package com.voyageone.components.intltarget.bean.guest;

import java.util.List;

/**
 * @author aooer 2016/4/13.
 * @version 2.0.0
 * @since 2.0.0
 */
public class TargetGuestPaymentTender {

    private List<TargetGuestPaymentContactAddress> contact;

    private List<TargetGuestPaymentProtocolData> checkoutProfile;

    public List<TargetGuestPaymentProtocolData> getCheckoutProfile() {
        return checkoutProfile;
    }

    public void setCheckoutProfile(List<TargetGuestPaymentProtocolData> checkoutProfile) {
        this.checkoutProfile = checkoutProfile;
    }

    public List<TargetGuestPaymentContactAddress> getContact() {
        return contact;
    }

    public void setContact(List<TargetGuestPaymentContactAddress> contact) {
        this.contact = contact;
    }
}
