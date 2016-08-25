package com.voyageone.web2.admin.views.user;

import com.voyageone.security.model.ComLogModel;
import com.voyageone.security.model.ComOrganizationModel;
import com.voyageone.service.bean.com.AdminOrgBean;
import com.voyageone.service.impl.com.user.AdminOrgService;
import com.voyageone.service.model.com.PageModel;
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
import java.util.List;
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


    @RequestMapping(AdminUrlConstants.User.Org.INIT)
    public AjaxResponse init() throws Exception {
        PageModel<AdminOrgBean> result = adminOrgService.searchOrg(1, DEFAULT_PAGE_SIZE );
        return success(result);
    }

    @RequestMapping(AdminUrlConstants.User.Org.SEARCH_ORG)
    public AjaxResponse searchOrg(@RequestBody Map requestBean) throws Exception {

        ComOrganizationModel model = new ComOrganizationModel();

        BeanUtils.populate(model, requestBean);

        Integer  pageNum = (Integer) requestBean.getOrDefault("pageNum", 1);
        Integer  pageSize = (Integer) requestBean.getOrDefault("pageSize", DEFAULT_PAGE_SIZE);

       PageModel<AdminOrgBean> result = adminOrgService.searchOrg(model, pageNum, pageSize );

        return success(result);
    }

    @RequestMapping(AdminUrlConstants.User.Org.ADD_ORG)
    public AjaxResponse addOrg(@RequestBody Map requestBean) throws Exception {

        ComOrganizationModel model = new ComOrganizationModel();

        BeanUtils.populate(model, requestBean);

        adminOrgService.addOrg(model);

        return success(true);
    }

    @RequestMapping(AdminUrlConstants.User.Org.UPDATE_ORG)
    public AjaxResponse updateOrg(@RequestBody Map requestBean) throws Exception {

        ComOrganizationModel model = new ComOrganizationModel();

        BeanUtils.populate(model, requestBean);

        adminOrgService.updateOrg(model);

        return success(true);
    }

    @RequestMapping(AdminUrlConstants.User.Org.DELETE_ORG)
    public AjaxResponse deleteOrg(@RequestBody List<Integer> bean) throws Exception {

        String username = getUser().getUserName();
        adminOrgService.deleteOrg(bean, username);

        return success(true);
    }
}


