package com.voyageone.web2.admin.views.user;

import com.google.common.base.Preconditions;
import com.voyageone.service.bean.com.AdminUserBean;
import com.voyageone.service.impl.com.user.AdminUserService;
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

import java.util.List;

/**
 * Created by Ethan Shi on 2016-08-15.
 */

@RestController
@RequestMapping(value = AdminUrlConstants.User.Self.ROOT, method = RequestMethod.POST)
public class AdminUserController extends AdminController {


    @Autowired
    AdminUserService adminUserService;

    @RequestMapping(AdminUrlConstants.User.Self.SEARCH_USER)
    public AjaxResponse searchUser(@RequestBody UserFormBean form) {
        int pageNum = 1;
        int pageSize = DEFAULT_PAGE_SIZE;

        if(form.getPageNum() != null  && form.getPageNum() > 1)
        {
            pageNum = form.getPageNum();
        }

        if(form.getPageSize() != null  && form.getPageNum() > 0)
        {
            pageSize = form.getPageSize();
        }

        // 检索用户信息

        PageModel<AdminUserBean> userPage = adminUserService.searchUser(form.getUserAccount(), form.getActive(),
                form.getOrgId(),form.getRoleId(),  form.getChannelId(), form.getStoreId(),pageNum, pageSize );

        return success(userPage);
    }

    @RequestMapping(AdminUrlConstants.User.Self.INIT)
    public AjaxResponse init(@RequestBody UserFormBean form)  {
        // 检索用户信息
        PageModel<AdminUserBean> userPage = adminUserService.searchUser(null, null, null, null, null,null, 1, DEFAULT_PAGE_SIZE);

        return success(userPage);
    }

    @RequestMapping(AdminUrlConstants.User.Self.ADD_USER)
    public AjaxResponse addUser(@RequestBody AdminUserBean bean){
        // 验证参数
        Preconditions.checkNotNull(bean.getUserName());
        Preconditions.checkNotNull(bean.getUserAccount());
        Preconditions.checkNotNull(bean.getEmail());
        Preconditions.checkNotNull(bean.getOrgId());
        Preconditions.checkNotNull(bean.getRoleId());

        String username = getUser().getUserName();
        adminUserService.addUser(bean, username);
        return success(true);
    }


    @RequestMapping(AdminUrlConstants.User.Self.UPDATE_USER)
    public AjaxResponse updateUser(@RequestBody AdminUserBean bean)  {
        // 验证参数
        Preconditions.checkNotNull(bean.getUserName());
        Preconditions.checkNotNull(bean.getUserAccount());
        Preconditions.checkNotNull(bean.getEmail());
        Preconditions.checkNotNull(bean.getOrgId());
        Preconditions.checkNotNull(bean.getRoleId());

        String username = getUser().getUserName();
        adminUserService.updateUser(bean, username);

        return success(true);
    }


    @RequestMapping(AdminUrlConstants.User.Self.DELETE_USER)
    public AjaxResponse deleteUser(@RequestBody List<Integer> bean)  {
        // 验证参数
        Preconditions.checkNotNull(bean);
        String username = getUser().getUserName();
        adminUserService.deleteUser(bean, username);
        return success(true);
    }


    @RequestMapping(AdminUrlConstants.User.Self.SHOW_AUTH)
    public AjaxResponse showAuth(@RequestBody Integer userId)  {
        // 验证参数
        Preconditions.checkNotNull(userId);

        return success(true);
    }
}
