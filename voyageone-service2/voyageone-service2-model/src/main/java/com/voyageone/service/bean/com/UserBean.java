package com.voyageone.service.bean.com;

/**
 * 映射 ct_user 表
 * Created on 11/27/15.
 *
 * @author Jonas
 * @version 2.0.0
 */
public class UserBean {
    private int id;

    private String username;

    private String password;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
