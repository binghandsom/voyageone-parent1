package com.voyageone.web2.cms.views.promotion.task;

import com.voyageone.web2.base.BaseController;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsUrlConstants.PROMOTION.TASK.INDEX;
import com.voyageone.service.model.cms.CmsBtTasksModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
    private CmsTaskService taskService;

    /**
     * 这里暂时没实现分页, 临时使用全部
     */
    @RequestMapping(INDEX.PAGE)
    public AjaxResponse page(@RequestBody Map<String,Object> searchInfo) {
        searchInfo.put("channelId",getUser().getSelChannelId());
        List<CmsBtTasksModel> models = taskService.getAllTasks(searchInfo);
        return success(models);
    }
}
