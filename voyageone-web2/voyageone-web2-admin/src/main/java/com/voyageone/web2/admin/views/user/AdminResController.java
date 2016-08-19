package com.voyageone.web2.admin.views.user;

import com.google.common.base.Preconditions;
import com.voyageone.security.model.ComResourceModel;
import com.voyageone.service.impl.com.user.AdminResService;
import com.voyageone.web2.admin.AdminController;
import com.voyageone.web2.admin.AdminUrlConstants;
import com.voyageone.web2.base.ajax.AjaxResponse;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Ethan Shi on 2016-08-17.
 */

@RestController
@RequestMapping(value = AdminUrlConstants.User.Res.ROOT, method = RequestMethod.POST)
public class AdminResController extends AdminController {


    @Autowired
    AdminResService adminResService;

    /**
     * 检索菜单资源
     *
     * @param requestBean
     * @return
     */
    @RequestMapping(AdminUrlConstants.User.Res.SEARCH_RES)
    public AjaxResponse searchRes(@RequestBody Map requestBean) {

        String application = requestBean.getOrDefault("application", "").toString();
        Map<String, Object> response = new HashMap<>();
        response.put("treeList", adminResService.searchRes(application));

        return success(response);
    }

    @RequestMapping(AdminUrlConstants.User.Res.INIT)
    public AjaxResponse init(@RequestBody Map requestBean) {
        Map<String, Object> response = new HashMap<>();
        response.put("treeList", adminResService.searchRes(null));

        return success(response);
    }

    @RequestMapping(AdminUrlConstants.User.Res.ADD_RES)
    public AjaxResponse addRes(@RequestBody Map requestBean) throws Exception {

        ComResourceModel model = new ComResourceModel();

        BeanUtils.populate(model, requestBean);

        model.setCreater(getUser().getUserName());

        Map<String, Object> response = new HashMap<>();

        adminResService.addRes(model);

        return success(response);
    }

    @RequestMapping(AdminUrlConstants.User.Res.UPDATE_RES)
    public AjaxResponse updateRes(@RequestBody Map requestBean) throws Exception {

        ComResourceModel model = new ComResourceModel();

        BeanUtils.populate(model, requestBean);

        model.setCreater(getUser().getUserName());

        Map<String, Object> response = new HashMap<>();

        adminResService.updateRes(model);

        return success(response);
    }


    @RequestMapping(AdminUrlConstants.User.Res.DELETE_RES)
    public AjaxResponse deleteUser(@RequestBody List<Integer> bean)  {

        String username = getUser().getUserName();
        adminResService.deleteRes(bean, username);
        return success(true);
    }


}