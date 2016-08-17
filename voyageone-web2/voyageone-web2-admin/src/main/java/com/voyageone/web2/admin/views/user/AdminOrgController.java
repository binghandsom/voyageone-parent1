package com.voyageone.web2.admin.views.user;

import com.voyageone.service.impl.com.user.AdminOrgService;
import com.voyageone.web2.admin.AdminController;
import com.voyageone.web2.admin.AdminUrlConstants;
import com.voyageone.web2.base.ajax.AjaxResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Ethan Shi on 2016-08-17.
 */

@RestController
@RequestMapping(value = AdminUrlConstants.User.Org.ROOT, method = RequestMethod.POST)
public class AdminOrgController extends AdminController {


    @Autowired
    AdminOrgService adminOrgService;

    @RequestMapping(AdminUrlConstants.User.Org.GET_ALL_ORG)
    public AjaxResponse getAllOrg()  {
        return  success(adminOrgService.getAllOrg());
    }
}
