package com.voyageone.web2.admin.views.user;

import com.voyageone.security.model.ComLogModel;
import com.voyageone.security.model.ComOrganizationModel;
import com.voyageone.service.impl.com.user.AdminOrgService;
import com.voyageone.web2.admin.AdminController;
import com.voyageone.web2.admin.AdminUrlConstants;
import com.voyageone.web2.base.ajax.AjaxResponse;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

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


    @RequestMapping(AdminUrlConstants.User.Org.SEARCH_ORG)
    public AjaxResponse searchOrg(@RequestBody Map requestBean) throws Exception {

        ComOrganizationModel model = new ComOrganizationModel();

        BeanUtils.populate(model, requestBean);

        Integer  pageNum = (Integer) requestBean.getOrDefault("pageNum", 1);
        Integer  pageSize = (Integer) requestBean.getOrDefault("pageSize", DEFAULT_PAGE_SIZE);

        return success(true);
    }
}


