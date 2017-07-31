package com.voyageone.web2.cms.views.promotion.task;

import com.voyageone.service.bean.cms.CmsBtTasksBean;
import com.voyageone.service.impl.cms.TaskService;
import com.voyageone.web2.base.BaseController;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsUrlConstants.PROMOTION.TASK.INDEX;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by jonasvlag on 16/3/1.
 *
 * @version 2.0.0
 * @since 2.0.0
 */
@RestController
@RequestMapping(value = INDEX.ROOT, method = RequestMethod.POST)
public class CmsTaskController extends BaseController {

    @Autowired
    private CmsTaskService cmsTaskService;

    @Autowired
    private TaskService taskService;

    /**
     * 这里暂时没实现分页, 临时使用全部
     */
    @RequestMapping(INDEX.PAGE)
    public AjaxResponse page(@RequestBody Map<String,Object> searchInfo) {

        searchInfo.put("channelId",getUser().getSelChannelId());
//        if (!StringUtils.isEmpty(String.valueOf(searchInfo.get("status"))))
//            searchInfo.put("status", Integer.valueOf(String.valueOf(searchInfo.get("status"))));
        List<CmsBtTasksBean> models = cmsTaskService.getAllTasks(searchInfo);
        if(models == null) models = new ArrayList<>();
        models.sort((o1, o2) -> o1.getActivityStart().compareTo(o2.getActivityStart())*-1);
        return success(models);
    }

    @RequestMapping(INDEX.UPDATE_STATUS)
    public AjaxResponse updateStatus(@RequestBody Map<String, Object> updateInfo) {
        Integer taskId = Integer.valueOf(updateInfo.get("taskId").toString());
        Integer status = Integer.valueOf(updateInfo.get("status").toString());

        return success(taskService.updateTaskStatus(taskId, status, getUser().getUserName()));
    }
}
