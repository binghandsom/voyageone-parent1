package com.voyageone.components.intltarget.bean;

import java.util.List;

/**
 * @author aooer 2016/4/8.
 * @version 2.0.0
 * @since 2.0.0
 */
public class TargetGuestContact {

    private List<TargetGuestAddress> contact;

    public List<TargetGuestAddress> getContact() {
        return contact;
    }

    public void setContact(List<TargetGuestAddress> contact) {
        this.contact = contact;
    }
}
