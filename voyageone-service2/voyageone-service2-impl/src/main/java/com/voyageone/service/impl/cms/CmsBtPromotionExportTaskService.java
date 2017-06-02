package com.voyageone.service.impl.cms;

import com.voyageone.service.dao.cms.CmsBtPromotionExportTaskDao;
import com.voyageone.service.daoext.cms.CmsBtJmPromotionExportTaskDaoExt;
import com.voyageone.service.daoext.cms.CmsBtPromotionExportTaskDaoExt;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.cms.CmsBtPromotionExportTaskModel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 活动(非聚美)商品导出Service
 *
 * @Author rex.wu
 * @Create 2017-05-17 15:59
 */
@Service
public class CmsBtPromotionExportTaskService extends BaseService {

    @Autowired
    private CmsBtPromotionExportTaskDao cmsBtPromotionExportTaskDao;
    @Autowired
    private CmsBtPromotionExportTaskDaoExt cmsBtPromotionExportTaskDaoExt;


    public CmsBtPromotionExportTaskModel getById(Integer cmsBtPromotionExportTaskId) {
        return cmsBtPromotionExportTaskDao.select(cmsBtPromotionExportTaskId);
    }

    public List<CmsBtPromotionExportTaskModel> getExportTaskByUser(String channelId, Integer promotionId,  String userName, int start, Integer pageSize) {
        Map<String,Object> param = new HashMap<>();
        param.put("cmsBtPromotionId", promotionId);
        param.put("creater",userName);
        param.put("start",start);
        param.put("pageSize",pageSize);
        return cmsBtPromotionExportTaskDaoExt.selectList(param);
    }

    public int getExportTaskByUserCnt(String channelId, Integer promotionId, String userName) {
        Map<String,Object>param = new HashMap<>();
        param.put("cmsBtPromotionId", promotionId);
        param.put("creater",userName);
        return cmsBtPromotionExportTaskDao.selectCount(param);
    }
}
