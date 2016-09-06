package com.voyageone.web2.admin.views.user;

import com.google.common.base.Preconditions;
import com.voyageone.base.dao.mysql.paginator.MySqlPageHelper;
import com.voyageone.security.model.ComResourceModel;
import com.voyageone.security.model.ComRoleModel;
import com.voyageone.service.bean.com.AdminRoleBean;
import com.voyageone.service.impl.com.user.AdminRoleService;
import com.voyageone.service.model.com.PageModel;
import com.voyageone.web2.admin.AdminController;
import com.voyageone.web2.admin.AdminUrlConstants;
import com.voyageone.web2.admin.bean.user.UserFormBean;
import com.voyageone.web2.base.ajax.AjaxResponse;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Ethan Shi on 2016-08-17.
 */

@RestController
@RequestMapping(value = AdminUrlConstants.User.Role.ROOT, method = RequestMethod.POST)
public class AdminRoleController extends AdminController {

    @Autowired
    AdminRoleService adminRoleService;

    @RequestMapping(AdminUrlConstants.User.Role.INIT)
    public AjaxResponse searchUser() {
        PageModel<AdminRoleBean> result  = adminRoleService.searchRole(1, DEFAULT_PAGE_SIZE );
        return success(result);
    }

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

        return success(result);
    }

    @RequestMapping(AdminUrlConstants.User.Role.ADD_ROLE)
    public AjaxResponse addRole(@RequestBody Map requestMap) throws Exception {


        ComRoleModel model = new ComRoleModel();

        BeanUtils.populate(model, requestMap);
        model.setCreater(getUser().getUserName());

        List<String>  applications = (List<String>) requestMap.getOrDefault("applications", "");
        List<String> channelIds = (List<String>) requestMap.getOrDefault("channelIds", new ArrayList<>());
        List<String> storeIds = (List<String>) requestMap.getOrDefault("storeIds", new ArrayList<>());
        String allChannel = requestMap.getOrDefault("allChannel", "0").toString();
        String allStore = requestMap.getOrDefault("allStore", "0").toString();

        adminRoleService.addRole(model ,applications, channelIds,  storeIds , allChannel ,allStore );


        return success(true);
    }


    @RequestMapping(AdminUrlConstants.User.Role.UPDATE_ROLE)
    public AjaxResponse updateRole(@RequestBody Map requestMap) throws Exception {


        ComRoleModel model = new ComRoleModel();

        BeanUtils.populate(model, requestMap);
        model.setCreater(getUser().getUserName());

        List<String>  applications = (List<String>) requestMap.getOrDefault("applications", "");
        List<String> channelIds = (List<String>) requestMap.getOrDefault("channelIds", new ArrayList<>());
        List<String> storeIds = (List<String>) requestMap.getOrDefault("storeIds", new ArrayList<>());
        String allChannel = requestMap.getOrDefault("allChannel", "0").toString();
        String allStore = requestMap.getOrDefault("allStore", "0").toString();
        adminRoleService.updateRole(model ,applications, channelIds,  storeIds , allChannel ,allStore );
        return success(true);
    }


    @RequestMapping(AdminUrlConstants.User.Role.DELETE_ROLE)
    public AjaxResponse deleteUser(@RequestBody List<Integer> bean)  {
        String username = getUser().getUserName();
        adminRoleService.deleteRole(bean, username);
        return success(true);
    }

    @RequestMapping(AdminUrlConstants.User.Role.GET_ALL_ROLE)
    public AjaxResponse getAllRole()  {
        return success(adminRoleService.getAllRole());
    }

    @RequestMapping(AdminUrlConstants.User.Role.GET_ALL_ROLE_TYPE)
    public AjaxResponse getAllRoleType()  {
        List<Map<String, Object>> list = new ArrayList<>();
        list.add(new HashMap(){{put("id", 1); put("name", "IT管理员"); } });
        list.add(new HashMap(){{put("id", 2);put("name", "运营人员"); } });
        list.add(new HashMap(){{put("id", 3);put("name", "客服人员"); } });
        list.add(new HashMap(){{put("id", 4); put("name", "财务人员");  } });
        return success(list);
    }


    @RequestMapping(AdminUrlConstants.User.Role.SET_AUTH)
    public AjaxResponse setAuth(@RequestBody Map requestMap)  {

        List<Integer>  roleIds = (List<Integer>) requestMap.get("roleIds");
        List<Integer>  resIds = (List<Integer>) requestMap.get("resIds");
        List<String>  apps = (List<String>) requestMap.get("apps");
        String username = getUser().getUserName();
        adminRoleService.setAuth(apps, roleIds, resIds, username);
        return success(true);
    }

    @RequestMapping(AdminUrlConstants.User.Role.ADD_AUTH)
    public AjaxResponse addAuth(@RequestBody Map requestMap)  {

        List<Integer>  roleIds = (List<Integer>) requestMap.get("roleIds");
        List<Integer>  resIds = (List<Integer>) requestMap.get("resIds");
        String username = getUser().getUserName();
        adminRoleService.addAuth(roleIds, resIds, username);
        return success(true);
    }


    @RequestMapping(AdminUrlConstants.User.Role.REMOVE_AUTH)
    public AjaxResponse removeAuth(@RequestBody Map requestMap)  {

        List<Integer>  roleIds = (List<Integer>) requestMap.get("roleIds");
        List<Integer>  resIds = (List<Integer>) requestMap.get("resIds");
        String username = getUser().getUserName();
        adminRoleService.removeAuth(roleIds, resIds, username);
        return success(true);
    }

}
