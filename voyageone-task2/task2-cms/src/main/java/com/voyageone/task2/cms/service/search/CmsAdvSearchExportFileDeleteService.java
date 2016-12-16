package com.voyageone.task2.cms.service.search;

import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.Properties;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.FileUtils;
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
import java.util.ArrayList;
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

        $info("CmsAdvSearchExportFileDeleteService开始");
        Map<String, Object> map = new HashMap<String, Object>();
        List<Integer> taskTypes = new ArrayList<Integer>();
        taskTypes.add(CmsBtExportTaskService.FEED); // 额外新增
        taskTypes.add(CmsBtExportTaskService.ADV_SEARCH);
        map.put("taskTypes", taskTypes);
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.add(Calendar.MONTH, -1);
        map.put("created", cal.getTime());
        List<CmsBtExportTaskModel> tasksModels = cmsBtExportTaskDao.queryFileDeleting(map);
        if (CollectionUtils.isNotEmpty(tasksModels)) {
            $info("需要删除的文件数："+tasksModels.size());
            String advSearchExportPath = Properties.readValue(CmsProperty.Props.SEARCH_ADVANCE_EXPORT_PATH);
            String feedExportPath =  CmsBtExportTaskService.savePath;
            for (CmsBtExportTaskModel task:tasksModels) {
                $info(task.getFileName());
                if (StringUtils.isNotBlank(task.getFileName()) && task.getStatus() == 1) {
                    String filePath = "";
                    if (task.getTaskType() != null && task.getTaskType().intValue() == CmsBtExportTaskService.FEED) {
                        filePath = feedExportPath + task.getFileName();
                    }else if (task.getTaskType() != null && task.getTaskType().intValue() == CmsBtExportTaskService.ADV_SEARCH) {
                        filePath = advSearchExportPath + task.getFileName();
                    }
                    boolean exists = true;
                    String comment = "";
                    File file = new File(filePath);
                    if (file.isFile() && file.exists()) {
                        boolean deleted = file.delete();
                        exists = !deleted;
                        comment = "文件过期，系统删除！";
                    }else {
                        exists = false;
                        comment = "文件不存在！";
                    }
                    if (!exists) {
                        CmsBtExportTaskModel target = new CmsBtExportTaskModel();
                        target.setStatus(-1); // 导出文件已被系统定期删除
                        target.setId(task.getId());
                        target.setComment(comment);
                        target.setModifier("SYSTEM");
                        cmsBtExportTaskDao.update(target);
                    }
                }
            }
        }else{
            $info("没有需要删除的文件");
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
