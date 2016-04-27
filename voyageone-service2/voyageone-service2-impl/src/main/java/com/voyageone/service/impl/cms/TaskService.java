package com.voyageone.service.impl.cms;

import com.voyageone.common.components.transaction.VOTransactional;
import com.voyageone.service.bean.cms.CmsBtTasksBean;
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
    private CmsBtTasksDaoExt cmsBtTaskDao;

    public CmsBtTasksBean getTaskWithPromotion(int task_id) {
        return cmsBtTaskDao.selectByIdWithPromotion(task_id);
    }

    public List<CmsBtTasksBean> getTasks(int promotionId, String taskName, String channelId, int taskType) {
        return cmsBtTaskDao.selectByName(promotionId, taskName, channelId, taskType);
    }

    public List<CmsBtTasksBean> getTasksWithPromotionByCondition(Map<String, Object> searchInfo) {
        return cmsBtTaskDao.selectTaskWithPromotionByChannel(searchInfo);
    }

    @VOTransactional
    public int addTask(CmsBtTasksBean cmsBtTaskModel) {
        return cmsBtTaskDao.insert(cmsBtTaskModel);
    }
}
