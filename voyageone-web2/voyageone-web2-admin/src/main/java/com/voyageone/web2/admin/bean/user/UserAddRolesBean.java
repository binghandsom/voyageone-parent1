package com.voyageone.web2.admin.bean.user;

import java.util.List;

/**
 * Created by Ethan Shi on 2016-12-23.
 */
public class UserAddRolesBean {

    private List<Integer> roleIds;

    private List<Integer> userIds;

    public List<Integer> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(List<Integer> roleIds) {
        this.roleIds = roleIds;
    }

    public List<Integer> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<Integer> userIds) {
        this.userIds = userIds;
    }
}
