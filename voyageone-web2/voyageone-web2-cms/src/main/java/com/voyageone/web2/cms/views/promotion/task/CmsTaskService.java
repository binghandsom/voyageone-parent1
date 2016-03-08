package com.voyageone.web2.cms.views.promotion.task;

import com.voyageone.web2.base.BaseAppService;
import com.voyageone.web2.cms.wsdl.dao.CmsBtTaskDao;
import com.voyageone.web2.cms.wsdl.models.CmsBtTaskModel;
import com.voyageone.web2.core.bean.UserSessionBean;
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
    private CmsBtTaskDao taskDao;

    public List<CmsBtTaskModel> getAllTasks(Map<String,Object>searchInfo) {

        return taskDao.selectTaskWithPromotionByChannel(searchInfo);
    }

    public CmsBtTaskModel getTaskWithPromotion(int task_id) {

        return taskDao.selectByIdWithPromotion(task_id);
    }
}
