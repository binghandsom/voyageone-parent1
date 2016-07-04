package com.voyageone.service.impl.cms;

import com.voyageone.service.daoext.cms.CmsBtExportTaskDaoExt;
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
    private CmsBtExportTaskDaoExt cmsBtExportTaskDao;

    public static final int FEED = 0;

    public static final String templatePath = "/usr/feed.xlsx";

    public static final String savePath = "/usr/";

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
        return getExportTaskByUser(channelId,taskType,user,null,null);
    }

    public List<CmsBtExportTaskModel> getExportTaskByUser(String channelId, Integer taskType, String user,Integer pageStart, Integer pageSize){
        Map<String,Object>param = new HashMap<>();
        param.put("channelId",channelId);
        param.put("taskType",taskType);
        param.put("creater",user);
        param.put("pageStart",pageStart);
        param.put("pageSize",pageSize);
        return cmsBtExportTaskDao.selectList(param);
    }

    public int getExportTaskByUserCnt(String channelId, Integer taskType, String user){
        Map<String,Object>param = new HashMap<>();
        param.put("channelId",channelId);
        param.put("taskType",taskType);
        param.put("creater",user);
        return cmsBtExportTaskDao.selectCnt(param);
    }
}
