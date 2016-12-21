package com.voyageone.web2.admin.views.user;

import com.google.common.base.Preconditions;
import com.voyageone.common.util.CommonUtil;
import com.voyageone.service.bean.com.AdminUserBean;
import com.voyageone.service.impl.com.user.AdminResService;
import com.voyageone.service.impl.com.user.AdminUserService;
import com.voyageone.service.bean.com.PaginationResultBean;
import com.voyageone.web2.admin.AdminController;
import com.voyageone.web2.admin.AdminUrlConstants;
import com.voyageone.web2.admin.bean.user.UserFormBean;
import com.voyageone.web2.base.ajax.AjaxResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Ethan Shi on 2016-08-15.
 */

@RestController
@RequestMapping(value = AdminUrlConstants.User.Self.ROOT, method = RequestMethod.POST)
public class AdminUserController extends AdminController {

//    private static final String DEFAULT_PASS = "1234567890";


    @Autowired
    AdminUserService adminUserService;

    @Autowired
    AdminResService adminResService;

    /**
     * 检索用户
     * @param form
     * @return
     */
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

        PaginationResultBean<AdminUserBean> userPage = adminUserService.searchUser(form.getUserAccount(), form.getActive(),
                form.getOrgId(),form.getRoleId(),  form.getChannelId(), form.getStoreId(), form.getApplication(), form.getCompanyId(), pageNum, pageSize );

        return success(userPage);
    }

    /**
     * 初始化页面
     * @return
     */
    @RequestMapping(AdminUrlConstants.User.Self.INIT)
    public AjaxResponse init()  {
        // 检索用户信息
        PaginationResultBean<AdminUserBean> userPage = adminUserService.searchUser(null, null, null, null, null,null,null,null, 1, DEFAULT_PAGE_SIZE);

        return success(userPage);
    }

    /**
     * 添加用户
     * @param bean
     * @return
     */
    @RequestMapping(AdminUrlConstants.User.Self.ADD_USER)
    public AjaxResponse addUser(@RequestBody AdminUserBean bean){
        // 验证参数
        Preconditions.checkNotNull(bean.getUserName());
        Preconditions.checkNotNull(bean.getUserAccount());
        Preconditions.checkNotNull(bean.getEmail());
        Preconditions.checkNotNull(bean.getOrgId());
        Preconditions.checkNotNull(bean.getRoleId());

        String username = getUser().getUserName();

        //生成随机密码
        String pass= CommonUtil.getRomdonPass(6);
        bean.setPassword(pass);
        adminUserService.addUser(bean, username);
        return success(true);
    }


    /**
     * 修改用户
     * @param bean
     * @return
     */
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


    /**
     * 软删除用户
     *
     * @param bean
     * @return
     */
    @RequestMapping(AdminUrlConstants.User.Self.DELETE_USER)
    public AjaxResponse deleteUser(@RequestBody List<Integer> bean)  {
        // 验证参数
        Preconditions.checkNotNull(bean);
        String username = getUser().getUserName();
        adminUserService.deleteUser(bean, username);
        return success(true);
    }


    /**
     * 管理员重置密码
     * 密码用明文回传给客户端了，这样会有安全隐患
     * @param bean
     * @return
     */
    @RequestMapping(AdminUrlConstants.User.Self.RESET_PASS)
    public AjaxResponse restPass(@RequestBody List<Integer> bean)  {
        // 验证参数
        Preconditions.checkNotNull(bean);
        String username = getUser().getUserName();
        //生成随机密码
        String pass= CommonUtil.getRomdonPass(6);

        adminUserService.restPass(bean, pass ,username);
        HashMap<String, Object> map = new HashMap<>();
        map.put("password", pass);
        return success(map);
    }


    /**
     * 根据Token查询用户
     *
     * @param requestMap
     * @return
     */
    @RequestMapping(AdminUrlConstants.User.Self.GET_USER_BY_TOKEN)
    public AjaxResponse getUserByToken(@RequestBody  Map requestMap)  {
        String token = requestMap.get("token").toString();
        return success(adminUserService.getUserByToken(token));
    }


    /**
     * 强制用户修改密码
     * @param requestMap
     * @return
     */
    @RequestMapping(AdminUrlConstants.User.Self.EDIT_PASS)
    public AjaxResponse editPass(@RequestBody  Map requestMap)  {
        String token = requestMap.get("token").toString();
        String pass = requestMap.get("password").toString();
        adminUserService.restPass(token, pass);
        return success(true);
    }

    /**
     * 用户自行修改密码
     * @param requestMap
     * @return
     */
    @RequestMapping(AdminUrlConstants.User.Self.MODIFY_PASS)
    public AjaxResponse modifyPass(@RequestBody  Map requestMap)  {
        String pass = requestMap.get("password").toString();
        String username = getUser().getUserName();
        List<Integer> list = new ArrayList<>();
        Integer userId = getUser().getUserId();
        list.add(userId);
        adminUserService.restPass(list, pass ,username);
        return success(true);
    }

    /**
     * 忘记密码
     *
     * @param requestMap
     * @return
     */
    @RequestMapping(AdminUrlConstants.User.Self.FORGET_PASS)
    public AjaxResponse forgetPass(@RequestBody Map requestMap)  {

        String userAccount = requestMap.get("userAccount").toString();
        adminUserService.forgetPass(userAccount);
        return success(true);
    }


    /**
     * 显示用户的菜单权限
     *
     * @param requestMap
     * @return
     */
    @RequestMapping(AdminUrlConstants.User.Self.GET_AUTH_BY_USER)
    public AjaxResponse getAuth(@RequestBody Map requestMap)  {
        // 验证参数
        String userAccount = requestMap.get("userAccount").toString();
        String application = requestMap.get("application").toString();

        return success(adminUserService.showUserAuth(userAccount, application));
    }


    /**
     * 取所有可用的系统
     *
     * @return
     */
    @RequestMapping(AdminUrlConstants.User.Self.GET_ALL_APP)
    public AjaxResponse getAllApp()  {
        return success(adminUserService.getAllApp());
    }

}
