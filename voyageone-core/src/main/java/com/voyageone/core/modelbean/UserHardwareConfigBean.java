package com.voyageone.core.modelbean;

/**
 * Created by Jonas on 4/13/2015.
 */
public class UserHardwareConfigBean {
    private int user_id;

    private String hardware_key;

    private String hardware_name;

    private String fix_val1;

    private String fix_val2;

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getHardware_key() {
        return hardware_key;
    }

    public void setHardware_key(String hardware_key) {
        this.hardware_key = hardware_key;
    }

    public String getHardware_name() {
        return hardware_name;
    }

    public void setHardware_name(String hardware_name) {
        this.hardware_name = hardware_name;
    }

    public String getFix_val1() {
        return fix_val1;
    }

    public void setFix_val1(String fix_val1) {
        this.fix_val1 = fix_val1;
    }

    public String getFix_val2() {
        return fix_val2;
    }

    public void setFix_val2(String fix_val2) {
        this.fix_val2 = fix_val2;
    }
}
