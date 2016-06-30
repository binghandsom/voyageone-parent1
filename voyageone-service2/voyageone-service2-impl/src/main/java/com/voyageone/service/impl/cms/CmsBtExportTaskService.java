package com.voyageone.service.impl.cms;

import com.voyageone.service.dao.cms.CmsBtExportTaskDao;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.cms.CmsBtExportTaskModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author james.li on 2016/6/27.
 * @version 2.0.0
 */
@Service
public class CmsBtExportTaskService extends BaseService {

    @Autowired
    private CmsBtExportTaskDao cmsBtExportTaskDao;

    public static final int FEED = 0;

    public Integer add(CmsBtExportTaskModel cmsBtExportTaskModel){
        return cmsBtExportTaskDao.insert(cmsBtExportTaskModel);
    }

    public List<CmsBtExportTaskModel> getExportByChannelTypeStatus(String channelId, Integer taskType, Integer status){
        Map<String,Object>param = new HashMap<>();
        param.put("channelId",channelId);
        param.put("taskType",taskType);
        param.put("status",status);
        return cmsBtExportTaskDao.selectList(param);
    }

    public Integer update(CmsBtExportTaskModel cmsBtExportTaskModel){
        return cmsBtExportTaskDao.update(cmsBtExportTaskModel);
    }

    public List<CmsBtExportTaskModel> getExportTaskByUser(String channelId, Integer taskType, String user){
        Map<String,Object>param = new HashMap<>();
        param.put("channelId",channelId);
        param.put("taskType",taskType);
        param.put("creater",user);
        return cmsBtExportTaskDao.selectList(param);
    }
}
