package com.voyageone.components.intltarget.bean.guest;

/**
 * @author aooer 2016/4/13.
 * @version 2.0.0
 * @since 2.0.0
 */
public class TargetGuestAccountRequest {

    private String firstName;

    private String middleName;

    private String lastName;

    private String logonId;

    private String logonPassword;

    private String LogonPasswordVerify;

    private String sendMeEmail;

    /***** getter setter ******/

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLogonId() {
        return logonId;
    }

    public void setLogonId(String logonId) {
        this.logonId = logonId;
    }

    public String getLogonPassword() {
        return logonPassword;
    }

    public void setLogonPassword(String logonPassword) {
        this.logonPassword = logonPassword;
    }

    public String getLogonPasswordVerify() {
        return LogonPasswordVerify;
    }

    public void setLogonPasswordVerify(String LogonPasswordVerify) {
        this.LogonPasswordVerify = LogonPasswordVerify;
    }

    public String getSendMeEmail() {
        return sendMeEmail;
    }

    public void setSendMeEmail(String sendMeEmail) {
        this.sendMeEmail = sendMeEmail;
    }
}
