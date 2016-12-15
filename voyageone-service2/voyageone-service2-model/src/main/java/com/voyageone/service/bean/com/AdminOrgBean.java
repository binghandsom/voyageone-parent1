package com.voyageone.service.bean.com;

import com.voyageone.service.model.user.ComOrganizationModel;

/**
 * Created by Ethan Shi on 2016-08-24.
 */
public class AdminOrgBean extends ComOrganizationModel {

    private String parentName;

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }
}
