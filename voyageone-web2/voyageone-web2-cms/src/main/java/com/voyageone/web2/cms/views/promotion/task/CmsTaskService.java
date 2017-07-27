package com.voyageone.web2.cms.views.promotion.task;

import com.voyageone.service.bean.cms.CmsBtTasksBean;
import com.voyageone.service.impl.cms.TaskService;
import com.voyageone.web2.base.BaseViewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by jonasvlag on 16/3/1.
 *
 * @version 2.0.0
 * @since 2.0.0
 */
@Service
class CmsTaskService extends BaseViewService {

    @Autowired
    private TaskService taskService;

    List<CmsBtTasksBean> getAllTasks(Map<String, Object> searchInfo) {
        return taskService.getTasksWithPromotionByCondition(searchInfo);
    }

    CmsBtTasksBean getTaskWithPromotion(int task_id) {
        return taskService.getTaskWithPromotion(task_id);
    }


}
