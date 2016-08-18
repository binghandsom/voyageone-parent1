package com.voyageone.web2.admin.views.user;

import com.voyageone.base.dao.mysql.paginator.MySqlPageHelper;
import com.voyageone.service.bean.com.AdminRoleBean;
import com.voyageone.service.impl.com.user.AdminRoleService;
import com.voyageone.service.model.com.PageModel;
import com.voyageone.web2.admin.AdminController;
import com.voyageone.web2.admin.AdminUrlConstants;
import com.voyageone.web2.admin.bean.user.UserFormBean;
import com.voyageone.web2.base.ajax.AjaxResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Ethan Shi on 2016-08-17.
 */

@RestController
@RequestMapping(value = AdminUrlConstants.User.Role.ROOT, method = RequestMethod.POST)
public class AdminRoleController extends AdminController {

    @Autowired
    AdminRoleService adminRoleService;

    @RequestMapping(AdminUrlConstants.User.Role.SEARCH_ROLE)
    public AjaxResponse searchUser(@RequestBody UserFormBean form) {
        int pageNum = 1;
        int pageSize = DEFAULT_PAGE_SIZE;

        if(form.getPageNum() != null  && form.getPageNum() > 1)
        {
            pageNum = form.getPageNum();
        }

        if(form.getPageSize() != null  && form.getPageNum() > 0) {
            pageSize = form.getPageSize();
        }

        String roleName = form.getRoleName();
        Integer roleType = form.getRoleType();
        String channelId = form.getChannelId();
        Integer active = form.getActive();
        Integer storeId = form.getStoreId();
        String application = form.getApplication();



        PageModel<AdminRoleBean> result  = adminRoleService.searchRole(roleName, roleType, channelId,  active, storeId, application , pageNum, pageSize );



        return success(true);
    }
}
