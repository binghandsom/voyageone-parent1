package com.voyageone.web2.admin.views.user;

import com.google.common.base.Preconditions;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.service.bean.admin.AdminUserBean;
import com.voyageone.service.impl.admin.user.AdminUserService;
import com.voyageone.service.model.admin.PageModel;
import com.voyageone.service.model.admin.TmOrderChannelModel;
import com.voyageone.web2.admin.AdminController;
import com.voyageone.web2.admin.AdminUrlConstants;
import com.voyageone.web2.admin.bean.user.UserFormBean;
import com.voyageone.web2.base.ajax.AjaxResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Ethan Shi on 2016-08-15.
 */

@RestController
@RequestMapping(value = AdminUrlConstants.User.Self.ROOT, method = RequestMethod.POST)
public class UserController  extends AdminController {

    @Autowired
    AdminUserService adminUserService;

    @RequestMapping(AdminUrlConstants.User.Self.SEARCH_USER_BY_PAGE)
    public AjaxResponse searchUserByPage(@RequestBody UserFormBean form) throws Exception {
        // 验证参数
        Preconditions.checkNotNull(form.getPageNum());
        Preconditions.checkNotNull(form.getPageSize());
        // 检索用户信息
        PageModel<AdminUserBean> userPage = adminUserService.searchUserByPage(form.getUserAccount(), form.getActive(),
                form.getOrgId(), form.getChannelId(), form.getStoreId(),form.getPageNum(), form.getPageSize() );

        return success(userPage);
    }

    @RequestMapping(AdminUrlConstants.User.Self.INIT)
    public AjaxResponse init(@RequestBody UserFormBean form) throws Exception {
        // 检索用户信息
        PageModel<AdminUserBean> userPage = adminUserService.searchUserByPage(null, null, null, null, null,form.getPageNum(), form.getPageSize());

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
