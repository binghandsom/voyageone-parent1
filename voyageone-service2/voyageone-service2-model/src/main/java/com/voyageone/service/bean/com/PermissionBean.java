package com.voyageone.service.bean.com;

/**
 * 用户的渠道权限信息
 * Created on 12/1/15.
 *
 * @author Jonas
 * @version 2.0.0
 */
public class PermissionBean {

    private String role;

    private String application;

    private String module;

    private String controller;

    private String action;

    private boolean enabled;

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getController() {
        return controller;
    }

    public void setController(String controller) {
        this.controller = controller;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}