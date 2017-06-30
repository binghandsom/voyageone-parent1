package com.voyageone.web2.cms.views.promotion.task;

import com.voyageone.common.configs.Carts;
import com.voyageone.common.configs.Enums.CartEnums;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.bean.cms.CmsBtTasksBean;
import com.voyageone.web2.base.BaseController;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsUrlConstants.PROMOTION.TASK.INDEX;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

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
        List<CmsBtTasksBean> models = taskService.getAllTasks(searchInfo);
        if(models == null) models = new ArrayList<>();
        models.sort((o1, o2) -> o1.getActivityStart().compareTo(o2.getActivityStart())*-1);
        return success(models);
    }
}
