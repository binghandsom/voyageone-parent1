package com.voyageone.service.impl.cms;

import com.voyageone.service.daoext.cms.CmsBtExportTaskDaoExt;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.cms.CmsBtExportTaskModel;
import com.voyageone.service.model.cms.SkuInventoryForCmsBean;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
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

    // feed检索
    public static final int FEED = 0;
    // 高级检索
    public static final int ADV_SEARCH = 1;

    public static final String templatePath = "/usr/web/contents/cms/file_template/feed-template.xlsx";
    // feed检索文件下载路径-- TODO 这两个路径应该写到配置文件里 “\voyageone-task2\task2-cms\src\main\resources\config\cms_keyvalue.properties”
    public static final String savePath = "/usr/web/contents/cms/feed_export/";

    public Integer add(CmsBtExportTaskModel cmsBtExportTaskModel){
        return cmsBtExportTaskDao.insert(cmsBtExportTaskModel);
    }

    public CmsBtExportTaskModel getExportById(int taskId) {
        return cmsBtExportTaskDao.select(taskId);
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

    /**
     * 检查指定用户是否有任务在执行
     * @return int 有任务在执行时返回任务数，没有则返回0
     */
    public int checkExportTaskByUser(String channelId, Integer taskType, String user) {
        Map<String, Object> param = new HashMap<>();
        param.put("channelId", channelId);
        param.put("taskType", taskType);
        param.put("creater", user);
        return cmsBtExportTaskDao.checkTaskByUser(param);
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

    /**
     * 批量查询sku级别库存
     * @param order_channel_id
     * @param codes
     * @return
     */
    public List<SkuInventoryForCmsBean> batchSelectInventory (String order_channel_id, List<String> codes) {
        if (StringUtils.isNotBlank(order_channel_id) && CollectionUtils.isNotEmpty(codes)) {
            Map<String, Object> params = new HashMap<>();
            params.put("order_channel_id", order_channel_id);
            params.put("codes", codes);
            return cmsBtExportTaskDao.getInventory(params);
        }
        return null;
    }
}
