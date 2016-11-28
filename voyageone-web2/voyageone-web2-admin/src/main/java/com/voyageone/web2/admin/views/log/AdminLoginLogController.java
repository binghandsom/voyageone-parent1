package com.voyageone.web2.admin.views.log;

import com.voyageone.security.model.ComLogModel;
import com.voyageone.security.model.ComLoginLogModel;
import com.voyageone.service.impl.com.log.AdminLogService;
import com.voyageone.service.impl.com.log.AdminLoginLogService;
import com.voyageone.web2.admin.AdminController;
import com.voyageone.web2.admin.AdminUrlConstants;
import com.voyageone.web2.base.ajax.AjaxResponse;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Created by Ethan Shi on 2016-08-23.
 */

@RestController
@RequestMapping(value = AdminUrlConstants.Log.Login.ROOT, method = RequestMethod.POST)
public class AdminLoginLogController extends AdminController {

    @Autowired
    AdminLoginLogService adminLoginLogService;

    @RequestMapping(AdminUrlConstants.Log.Login.INIT)
    public AjaxResponse init()  {
        return  success(adminLoginLogService.searchLog(1, DEFAULT_PAGE_SIZE));
    }

    @RequestMapping(AdminUrlConstants.Log.Login.SEARCH_LOG)
    public AjaxResponse searchLog(@RequestBody Map requestBean) throws Exception {

        ComLoginLogModel model = new ComLoginLogModel();

        BeanUtils.populate(model, requestBean);

        Integer  pageNum = (Integer) requestBean.getOrDefault("pageNum", 1);
        Integer  pageSize = (Integer) requestBean.getOrDefault("pageSize", DEFAULT_PAGE_SIZE);
        Long startTime =  (Long)requestBean.get("startTime");
        Long endTime =  (Long)requestBean.get("endTime");

        return  success(adminLoginLogService.searchLog(model, startTime, endTime, pageNum, pageSize));
    }

}
