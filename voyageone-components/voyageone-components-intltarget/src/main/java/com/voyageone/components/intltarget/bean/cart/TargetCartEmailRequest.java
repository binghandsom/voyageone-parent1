package com.voyageone.components.intltarget.bean.cart;

/**
 * @author aooer 2016/4/19.
 * @version 2.0.0
 * @since 2.0.0
 */
public class TargetCartEmailRequest {

    private String trackEmail;

    private String confirmEmail;

    //~~~~~~~getter setter

    public String getTrackEmail() {
        return trackEmail;
    }

    public void setTrackEmail(String trackEmail) {
        this.trackEmail = trackEmail;
    }

    public String getConfirmEmail() {
        return confirmEmail;
    }

    public void setConfirmEmail(String confirmEmail) {
        this.confirmEmail = confirmEmail;
    }
}
