package com.voyageone.web2.admin.views.user;

import com.voyageone.service.bean.com.AdminUserBean;
import com.voyageone.service.model.com.PageModel;
import com.voyageone.web2.admin.AdminController;
import com.voyageone.web2.admin.AdminUrlConstants;
import com.voyageone.web2.admin.bean.user.UserFormBean;
import com.voyageone.web2.base.ajax.AjaxResponse;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Ethan Shi on 2016-08-17.
 */
public class AdminRoleController extends AdminController {

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

        return success(true);
    }
}
