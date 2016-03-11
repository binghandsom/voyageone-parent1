package com.voyageone.web2.cms.views.promotion.task;

import com.voyageone.web2.base.BaseAppService;
import com.voyageone.service.dao.cms.CmsBtTasksDao;
import com.voyageone.service.model.cms.CmsBtTasksModel;
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
public class CmsTaskService extends BaseAppService {

    @Autowired
    private CmsBtTasksDao taskDao;

    public List<CmsBtTasksModel> getAllTasks(Map<String,Object>searchInfo) {

        return taskDao.selectTaskWithPromotionByChannel(searchInfo);
    }

    public CmsBtTasksModel getTaskWithPromotion(int task_id) {

        return taskDao.selectByIdWithPromotion(task_id);
    }
}
