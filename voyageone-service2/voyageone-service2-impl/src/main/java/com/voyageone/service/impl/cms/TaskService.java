package com.voyageone.service.impl.cms;

import com.voyageone.common.components.transaction.VOTransactional;
import com.voyageone.service.bean.cms.CmsBtTasksBean;
import com.voyageone.service.dao.cms.CmsBtTasksDao;
import com.voyageone.service.daoext.cms.CmsBtTasksDaoExt;
import com.voyageone.service.impl.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Tag Service
 *
 * @author chuanyu.liang 15/12/30
 * @version 2.0.0
 */
@Service
public class TaskService extends BaseService {

    @Autowired
    private CmsBtTasksDao tasksDao;

    @Autowired
    private CmsBtTasksDaoExt tasksDaoExt;

    public CmsBtTasksBean getTaskWithPromotion(int task_id) {
        return tasksDaoExt.selectWithPromotion(task_id);
    }

    public List<CmsBtTasksBean> getTasks(int promotionId, String taskName, String channelId, int taskType) {
        return tasksDaoExt.selectWithPromotion(promotionId, taskName, channelId, taskType);
    }

    public List<CmsBtTasksBean> getTasksWithPromotionByCondition(Map<String, Object> searchInfo) {
        return tasksDaoExt.selectWithPromotion(searchInfo);
    }

    @VOTransactional
    public int addTask(CmsBtTasksBean cmsBtTaskModel) {
        return tasksDao.insert(cmsBtTaskModel);
    }

    @VOTransactional
    public int updateConfig(CmsBtTasksBean cmsBtTasksBean) {
        return tasksDaoExt.updateConfig(cmsBtTasksBean);
    }
}
