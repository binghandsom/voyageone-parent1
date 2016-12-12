package com.voyageone.task2.cms.service.search;

import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.Properties;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.service.dao.cms.CmsBtTasksDao;
import com.voyageone.service.daoext.cms.CmsBtExportTaskDaoExt;
import com.voyageone.service.impl.CmsProperty;
import com.voyageone.service.impl.cms.CmsBtExportTaskService;
import com.voyageone.service.model.cms.CmsBtExportTaskModel;
import com.voyageone.service.model.cms.CmsBtTasksModel;
import com.voyageone.task2.base.BaseCronTaskService;
import com.voyageone.task2.base.modelbean.TaskControlBean;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by rex.wu on 2016/12/12.
 * job：定时清除高级检索导出产生的下载文件
 */
@Service
public class CmsAdvSearchExportFileDeleteService extends BaseCronTaskService {

    @Autowired
    private CmsBtExportTaskDaoExt cmsBtExportTaskDao;

    @Override
    protected void onStartup(List<TaskControlBean> taskControlList) throws Exception {

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("taskType", CmsBtExportTaskService.ADV_SEARCH);
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.add(Calendar.MONTH, -1);
        map.put("created", cal.getTime());
        List<CmsBtExportTaskModel> tasksModels = cmsBtExportTaskDao.queryFileDeleting(map);
        if (CollectionUtils.isNotEmpty(tasksModels)) {
            String exportPath = Properties.readValue(CmsProperty.Props.SEARCH_ADVANCE_EXPORT_PATH);
            for (CmsBtExportTaskModel task:tasksModels) {
                if (StringUtils.isNotBlank(task.getFileName()) && task.getStatus() == 1) {
                    File file = new File(exportPath + task.getFileName());
                    if (file.exists()) {
                        boolean deleted = file.delete();
                        if (deleted) {
                            CmsBtExportTaskModel target = new CmsBtExportTaskModel();
                            target.setStatus(-1); // 导出文件已被系统定期删除
                            target.setId(target.getId());
                            target.setModifier("SYSTEM");
                            cmsBtExportTaskDao.update(target);
                        }
                    }
                }
            }
        }
    }

    @Override
    protected String getTaskName() {
        return "cmsAdvSearchExportFileDeleteJob";
    }

    @Override
    protected SubSystem getSubSystem() {
        return SubSystem.CMS;
    }
}
