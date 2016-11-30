package com.voyageone.web2.admin.views.log;

import com.voyageone.service.model.user.ComLogModel;
import com.voyageone.service.impl.com.log.AdminLogService;
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
@RequestMapping(value = AdminUrlConstants.Log.Action.ROOT, method = RequestMethod.POST)
public class AdminLogController extends AdminController {

    @Autowired
    AdminLogService adminLogService;

    @RequestMapping(AdminUrlConstants.Log.Action.INIT)
    public AjaxResponse init()  {
        return  success(adminLogService.searchLog(1, DEFAULT_PAGE_SIZE));
    }

    @RequestMapping(AdminUrlConstants.Log.Action.SEARCH_LOG)
    public AjaxResponse searchLog(@RequestBody Map requestBean) throws Exception {

        ComLogModel model = new ComLogModel();

        BeanUtils.populate(model, requestBean);

        Integer  pageNum = (Integer) requestBean.getOrDefault("pageNum", 1);
        Integer  pageSize = (Integer) requestBean.getOrDefault("pageSize", DEFAULT_PAGE_SIZE);
        Long startTime =  (Long)requestBean.get("startTime");
        Long endTime =  (Long)requestBean.get("endTime");

        return  success(adminLogService.searchLog(model, startTime, endTime, pageNum, pageSize));
    }

    @RequestMapping(AdminUrlConstants.Log.Action.GET_LOG_DETAIL)
    public AjaxResponse getLog(@RequestBody Map requestBean)  {
        Integer  logId = (Integer) requestBean.get("logId");
        return  success(adminLogService.getLog(logId));
    }

}
