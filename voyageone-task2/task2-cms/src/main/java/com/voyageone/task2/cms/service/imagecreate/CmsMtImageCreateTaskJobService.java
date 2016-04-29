package com.voyageone.task2.cms.service.imagecreate;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.service.impl.cms.imagecreate.AliYunOSSFileService;
import com.voyageone.service.impl.cms.imagecreate.CmsMtImageCreateTaskDetailService;
import com.voyageone.service.impl.cms.imagecreate.CmsMtImageCreateTaskService;
import com.voyageone.service.impl.cms.imagecreate.ImageCreateFileService;
import com.voyageone.service.impl.com.mq.config.MqRoutingKey;
import com.voyageone.service.model.cms.CmsMtImageCreateTaskDetailModel;
import com.voyageone.service.model.cms.CmsMtImageCreateTaskModel;
import com.voyageone.task2.base.BaseMQCmsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
@Service
@RabbitListener(queues = MqRoutingKey.CMS_BATCH_CmsMtImageCreateTaskJob)
public class CmsMtImageCreateTaskJobService extends BaseMQCmsService {
    @Autowired
    ImageCreateFileService serviceImageCreateFile;
    @Autowired
    CmsMtImageCreateTaskDetailService serviceCmsMtImageCreateTaskDetail;
    @Autowired
    CmsMtImageCreateTaskService serviceCmsMtImageCreateTask;
    private static final Logger LOG = LoggerFactory.getLogger(AliYunOSSJobService.class);
    @Override
    public void onStartup(Map<String, Object> messageMap) throws Exception {

        int cmsMtImageCreateTaskId = (int) Double.parseDouble(messageMap.get("id").toString());
        List<CmsMtImageCreateTaskDetailModel> list = serviceCmsMtImageCreateTaskDetail.getListByCmsMtImageCreateTaskId(cmsMtImageCreateTaskId);
        CmsMtImageCreateTaskModel taskModel = serviceCmsMtImageCreateTask.get(cmsMtImageCreateTaskId);
        taskModel.setBeginTime(new Date());//1.执行开始时间
        List<Runnable> threads = new ArrayList<>();
        for (CmsMtImageCreateTaskDetailModel modelTaskDetail : list) {
            if (modelTaskDetail.getStatus() == 0) {
                threads.add(new Runnable() {
                    @Override
                    public void run() {
                        serviceImageCreateFile.createAndUploadImage(modelTaskDetail);
                    }
                });
            }
        }
        runWithThreadPool(threads, taskControlList);
        taskModel.setEndTime(new Date());//2.执行结束时间
        serviceCmsMtImageCreateTask.save(taskModel);
    }
    @Override
    public SubSystem getSubSystem() {
        return SubSystem.CMS;
    }
    @Override
    public String getTaskName() {
        return "CmsMtImageCreateTaskJobService";
    }
}
